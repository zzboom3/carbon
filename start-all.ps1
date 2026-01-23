param(
  [string]$VmHost = "192.168.100.128",
  [string]$MySqlHost = "192.168.100.128",
  [string]$MySqlPort = "3333",
  [string]$MySqlDb = "carbon",
  [string]$MySqlUser = "root",
  [string]$MySqlPassword = "root",
  [string]$RedisHost = "192.168.100.128",
  [string]$RedisPort = "6379",
  [string]$RedisPassword = "",
  [string]$NacosAddr = "192.168.100.128:8848",
  [string]$RocketMqNamesrv = "192.168.100.128:9876",
  [string]$FrontendPort = "9528",
  [string]$MailUsername = "",
  [string]$MailPassword = "",
  [string]$WeChatQrUrl = "",
  [string]$AiBaseUrl = "",
  [string]$AiChatPath = "",
  [string]$AiApiKey = "",
  [string]$AiModel = "",
  [int]$DisableLogs = 1,
  [string]$BackendServices = "carbon-auth,carbon-system,carbon-assets,carbon-trade,carbon-gate,carbon-mq,carbon-chainmaker",
  [string]$MavenOpts = "-Xms64m -Xmx256m",
  [string]$SpringJvmArgs = "-Xms64m -Xmx256m",
  [bool]$SkipBuild = $true,
  [bool]$SkipDependencyChecks = $true
)

$ErrorActionPreference = "Stop"

function Test-IsAdmin {
  $principal = [Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()
  return $principal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
}

function Find-MySqlClient {
  $candidates = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe",
    "C:\xampp\mysql\bin\mysql.exe"
  )
  foreach ($p in $candidates) {
    if (Test-Path $p) { return $p }
  }
  $cmd = Get-Command mysql -ErrorAction SilentlyContinue
  if ($cmd) { return $cmd.Source }
  return $null
}

function Test-MySqlLogin {
  param([string]$MySqlBin)
  $oldMySqlPwd = $env:MYSQL_PWD
  $env:MYSQL_PWD = $MySqlPassword
  try {
    & $MySqlBin "-h$MySqlHost" "-P$MySqlPort" "-u$MySqlUser" "-e" "SELECT 1;" 1>$null 2>$null
    return ($LASTEXITCODE -eq 0)
  }
  finally {
    $env:MYSQL_PWD = $oldMySqlPwd
  }
}

function Wait-Ports {
  param([int[]]$Ports, [int]$TimeoutSeconds = 120)
  $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
  while ((Get-Date) -lt $deadline) {
    $missing = @()
    foreach ($p in $Ports) {
      $r = Test-NetConnection -ComputerName "127.0.0.1" -Port $p -WarningAction SilentlyContinue
      if (-not $r.TcpTestSucceeded) { $missing += $p }
    }
    if ($missing.Count -eq 0) { return $true }
    Start-Sleep -Seconds 2
  }
  return $false
}

$repoRoot = Split-Path -Parent $PSCommandPath
Set-Location $repoRoot

$disableLogsFlag = ($DisableLogs -ne 0)

$startAllLog = Join-Path $repoRoot "start-all.log"
if (-not $disableLogsFlag) {
  Start-Transcript -Path $startAllLog -Append | Out-Null
}

try {
$mailLocal = Join-Path $repoRoot "carbon_back\tools\mail.local.ps1"
if ((-not $MailUsername) -and (-not $MailPassword) -and (Test-Path $mailLocal)) {
  . $mailLocal
}
if (-not $MailUsername) { $MailUsername = $env:CARBON_MAIL_USERNAME }
if (-not $MailPassword) { $MailPassword = $env:CARBON_MAIL_PASSWORD }

$aiLocal = Join-Path $repoRoot "carbon_back\tools\ai.local.ps1"
if ((-not $AiApiKey) -and (Test-Path $aiLocal)) {
  . $aiLocal
}
if (-not $AiBaseUrl) { $AiBaseUrl = $env:CARBON_AI_BASE_URL }
if (-not $AiChatPath) { $AiChatPath = $env:CARBON_AI_CHAT_PATH }
if (-not $AiApiKey) { $AiApiKey = $env:CARBON_AI_API_KEY }
if (-not $AiModel) { $AiModel = $env:CARBON_AI_MODEL }

if (-not $disableLogsFlag) {
  $frontLogDir = Join-Path $repoRoot "carbon_front\logs"
  New-Item -ItemType Directory -Force -Path $frontLogDir | Out-Null
}

$mysqlBin = Find-MySqlClient

$deps = @(
  @{ Name = "Nacos"; Host = $VmHost; Port = 8848 },
  @{ Name = "Redis"; Host = $VmHost; Port = 6379 },
  @{ Name = "RocketMQ"; Host = $VmHost; Port = 9876 }
)
if (-not $SkipDependencyChecks) {
  foreach ($d in $deps) {
    $r = Test-NetConnection $d.Host -Port $d.Port -WarningAction SilentlyContinue
    if (-not $r.TcpTestSucceeded) {
      throw "$($d.Name) unreachable: $($d.Host):$($d.Port)"
    }
  }
} else {
  foreach ($d in $deps) {
    $r = Test-NetConnection $d.Host -Port $d.Port -WarningAction SilentlyContinue
    if (-not $r.TcpTestSucceeded) {
      Write-Output "$($d.Name) unreachable: $($d.Host):$($d.Port) (skipped)"
    }
  }
}

if ($mysqlBin) {
  if (-not (Test-MySqlLogin -MySqlBin $mysqlBin)) {
    throw "MySQL login failed: $MySqlUser/$MySqlPassword @ ${MySqlHost}:${MySqlPort}"
  }
} else {
  Write-Output "mysql.exe not found, skipping MySQL login precheck."
}

if (-not $SkipBuild) {
  & (Join-Path $repoRoot "carbon_back\apache-maven-3.8.8\bin\mvn.cmd") -q -DskipTests -f (Join-Path $repoRoot "carbon_back\carbon-frame\pom.xml") install
}

$backendPortMap = @{
  "carbon-auth" = 9001
  "carbon-system" = 9002
  "carbon-assets" = 9003
  "carbon-trade" = 9005
  "carbon-gate" = 9091
  "carbon-mq" = 9006
  "carbon-chainmaker" = 9007
}
$backendServiceList = @()
if ($BackendServices) {
  $backendServiceList = ($BackendServices -split '[,\s]+' | Where-Object { $_ })
}
$backendPorts = @()
foreach ($s in $backendServiceList) {
  if ($backendPortMap.ContainsKey($s)) { $backendPorts += [int]$backendPortMap[$s] }
}
$backendPorts = $backendPorts | Select-Object -Unique
$frontPorts = @([int]$FrontendPort)
Write-Output ("backend services: {0}" -f ($backendServiceList -join ","))
Write-Output ("backend ports: {0}" -f ($backendPorts -join ","))
if (-not $backendPorts -or $backendPorts.Count -eq 0) {
  throw "No backend ports resolved from BackendServices."
}

if (Test-IsAdmin) {
  foreach ($p in ($backendPorts + $frontPorts)) {
    $c = Get-NetTCPConnection -LocalPort $p -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($c) {
      Stop-Process -Id $c.OwningProcess -Force -ErrorAction SilentlyContinue
    }
  }
}
else {
  Write-Output "Not running as Administrator: will not auto-kill existing port processes. If ports are occupied, rerun as Administrator."
}

& (Join-Path $repoRoot "carbon_back\tools\start-backend.ps1") `
  -MySqlHost $MySqlHost -MySqlPort $MySqlPort -MySqlDb $MySqlDb -MySqlUser $MySqlUser -MySqlPassword $MySqlPassword `
  -RedisHost $RedisHost -RedisPort $RedisPort -RedisPassword $RedisPassword `
  -NacosAddr $NacosAddr -RocketMqNamesrv $RocketMqNamesrv `
  -MailUsername $MailUsername -MailPassword $MailPassword `
  -AiBaseUrl $AiBaseUrl -AiChatPath $AiChatPath -AiApiKey $AiApiKey -AiModel $AiModel `
  -DisableLogs $DisableLogs `
  -Services $backendServiceList `
  -MavenOpts $MavenOpts `
  -SpringJvmArgs $SpringJvmArgs

$frontArgs = @(
  "-NoProfile",
  "-ExecutionPolicy", "Bypass",
  "-File", (Join-Path $repoRoot "carbon_front\tools\start-frontend.ps1")
)
if ($WeChatQrUrl) {
  $frontArgs += @("-WeChatQrUrl", $WeChatQrUrl)
}

if ($disableLogsFlag) {
  Start-Process -FilePath "powershell" -ArgumentList $frontArgs -WindowStyle Hidden
} else {
  $frontOut = Join-Path $frontLogDir "frontend.out.log"
  $frontErr = Join-Path $frontLogDir "frontend.err.log"
  Start-Process -FilePath "powershell" -ArgumentList $frontArgs -RedirectStandardOutput $frontOut -RedirectStandardError $frontErr -WindowStyle Hidden
}

if (-not (Wait-Ports -Ports ($backendPorts + $frontPorts) -TimeoutSeconds 300)) {
  throw "Ports not ready: $($backendPorts -join ',') / $FrontendPort"
}

Write-Output "OK"
Write-Output "frontend: http://localhost:$FrontendPort/"
Write-Output "gate: http://localhost:9091/"
if (-not $disableLogsFlag) { Write-Output "logs: $startAllLog" }
}
finally {
  if (-not $disableLogsFlag) { try { Stop-Transcript | Out-Null } catch { } }
}
