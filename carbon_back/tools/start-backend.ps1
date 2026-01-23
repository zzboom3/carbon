param(
  [string]$MavenCmd = "$PSScriptRoot\..\apache-maven-3.8.8\bin\mvn.cmd",
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
  [string]$EsUri = "http://localhost:9200",
  [string]$MailUsername = "",
  [string]$MailPassword = "",
  [string]$AiBaseUrl = "",
  [string]$AiChatPath = "",
  [string]$AiApiKey = "",
  [string]$AiModel = "",
  [int]$DisableLogs = 1,
  [string[]]$Services = @(),
  [string]$MavenOpts = "-Xms64m -Xmx256m",
  [string]$SpringJvmArgs = "-Xms64m -Xmx256m",
  [bool]$SpringBootFork = $false
)

$ErrorActionPreference = "Stop"

$mailLocal = Join-Path $PSScriptRoot "mail.local.ps1"
if ((-not $MailUsername) -and (-not $MailPassword) -and (Test-Path $mailLocal)) {
  . $mailLocal
}
if (-not $MailUsername) { $MailUsername = $env:CARBON_MAIL_USERNAME }
if (-not $MailPassword) { $MailPassword = $env:CARBON_MAIL_PASSWORD }

$aiLocal = Join-Path $PSScriptRoot "ai.local.ps1"
if ((-not $AiApiKey) -and (Test-Path $aiLocal)) {
  . $aiLocal
}
if (-not $AiBaseUrl) { $AiBaseUrl = $env:CARBON_AI_BASE_URL }
if (-not $AiChatPath) { $AiChatPath = $env:CARBON_AI_CHAT_PATH }
if (-not $AiApiKey) { $AiApiKey = $env:CARBON_AI_API_KEY }
if (-not $AiModel) { $AiModel = $env:CARBON_AI_MODEL }

$disableLogsFlag = ($DisableLogs -ne 0)

if (-not $disableLogsFlag) {
  $logsDir = Join-Path $PSScriptRoot "..\logs"
  New-Item -ItemType Directory -Force -Path $logsDir | Out-Null
}

$env:CARBON_MYSQL_HOST = $MySqlHost
$env:CARBON_MYSQL_PORT = $MySqlPort
$env:CARBON_MYSQL_DB = $MySqlDb
$env:CARBON_MYSQL_USER = $MySqlUser
$env:CARBON_MYSQL_PASSWORD = $MySqlPassword
$env:CARBON_REDIS_HOST = $RedisHost
$env:CARBON_REDIS_PORT = $RedisPort
$env:CARBON_REDIS_PASSWORD = $RedisPassword
$env:CARBON_NACOS_ADDR = $NacosAddr
$env:CARBON_RMQ_NAMESRV = $RocketMqNamesrv
$env:CARBON_ES_URI = $EsUri
$env:CARBON_MAIL_USERNAME = $MailUsername
$env:CARBON_MAIL_PASSWORD = $MailPassword
$env:CARBON_AI_BASE_URL = $AiBaseUrl
$env:CARBON_AI_CHAT_PATH = $AiChatPath
$env:CARBON_AI_API_KEY = $AiApiKey
$env:CARBON_AI_MODEL = $AiModel
$env:MAVEN_OPTS = $MavenOpts

$serviceConfigs = @(
  @{ Name = "carbon-auth"; Dir = (Join-Path $PSScriptRoot "..\carbon-auth") },
  @{ Name = "carbon-system"; Dir = (Join-Path $PSScriptRoot "..\carbon-system") },
  @{ Name = "carbon-assets"; Dir = (Join-Path $PSScriptRoot "..\carbon-assets") },
  @{ Name = "carbon-trade"; Dir = (Join-Path $PSScriptRoot "..\carbon-trade") },
  @{ Name = "carbon-gate"; Dir = (Join-Path $PSScriptRoot "..\carbon-gate") },
  @{ Name = "carbon-mq"; Dir = (Join-Path $PSScriptRoot "..\carbon-mq") },
  @{ Name = "carbon-chainmaker"; Dir = (Join-Path $PSScriptRoot "..\carbon-chainmaker") }
)

$selected = $serviceConfigs
if ($Services -and $Services.Count -gt 0) {
  $serviceList = @()
  foreach ($v in $Services) {
    $serviceList += ($v -split '[,\s]+' | Where-Object { $_ })
  }
  $serviceList = $serviceList | Select-Object -Unique
  $selected = $serviceConfigs | Where-Object { $serviceList -contains $_.Name }
}

foreach ($svc in $selected) {
  $name = $svc.Name
  $dir = $svc.Dir

  $cmd = @(
    "cd '$dir';",
    "& '$MavenCmd' -q -DskipTests spring-boot:run"
  ) -join " "

  if ($disableLogsFlag) {
    $p = Start-Process -FilePath "powershell" -ArgumentList @(
      "-NoProfile",
      "-ExecutionPolicy", "Bypass",
      "-Command", $cmd
    ) -WindowStyle Hidden -PassThru
    Write-Output ("{0} started pid {1}" -f $name, $p.Id)
  } else {
    $outFile = Join-Path $logsDir "$name.out.log"
    $errFile = Join-Path $logsDir "$name.err.log"
    $p = Start-Process -FilePath "powershell" -ArgumentList @(
      "-NoProfile",
      "-ExecutionPolicy", "Bypass",
      "-Command", $cmd
    ) -RedirectStandardOutput $outFile -RedirectStandardError $errFile -WindowStyle Hidden -PassThru
    Write-Output ("{0} started pid {1}" -f $name, $p.Id)
  }
}

Write-Output "backend started"
