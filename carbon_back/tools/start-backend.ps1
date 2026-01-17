param(
  [string]$MavenCmd = "$PSScriptRoot\..\apache-maven-3.8.8\bin\mvn.cmd",
  [string]$MySqlHost = "localhost",
  [string]$MySqlPort = "3306",
  [string]$MySqlDb = "carbon",
  [string]$MySqlUser = "root",
  [string]$MySqlPassword = "root",
  [string]$RedisHost = "localhost",
  [string]$RedisPort = "6379",
  [string]$RedisPassword = "",
  [string]$NacosAddr = "localhost:8848",
  [string]$RocketMqNamesrv = "localhost:9876",
  [string]$EsUri = "http://localhost:9200"
)

$ErrorActionPreference = "Stop"

$logsDir = Join-Path $PSScriptRoot "..\logs"
New-Item -ItemType Directory -Force -Path $logsDir | Out-Null

$services = @(
  @{ Name = "carbon-auth"; Dir = (Join-Path $PSScriptRoot "..\carbon-auth") },
  @{ Name = "carbon-system"; Dir = (Join-Path $PSScriptRoot "..\carbon-system") },
  @{ Name = "carbon-assets"; Dir = (Join-Path $PSScriptRoot "..\carbon-assets") },
  @{ Name = "carbon-trade"; Dir = (Join-Path $PSScriptRoot "..\carbon-trade") },
  @{ Name = "carbon-gate"; Dir = (Join-Path $PSScriptRoot "..\carbon-gate") }
)

foreach ($svc in $services) {
  $name = $svc.Name
  $dir = $svc.Dir
  $outFile = Join-Path $logsDir "$name.out.log"
  $errFile = Join-Path $logsDir "$name.err.log"

  $cmd = @(
    "`$env:CARBON_MYSQL_HOST='$MySqlHost';",
    "`$env:CARBON_MYSQL_PORT='$MySqlPort';",
    "`$env:CARBON_MYSQL_DB='$MySqlDb';",
    "`$env:CARBON_MYSQL_USER='$MySqlUser';",
    "`$env:CARBON_MYSQL_PASSWORD='$MySqlPassword';",
    "`$env:CARBON_REDIS_HOST='$RedisHost';",
    "`$env:CARBON_REDIS_PORT='$RedisPort';",
    "`$env:CARBON_REDIS_PASSWORD='$RedisPassword';",
    "`$env:CARBON_NACOS_ADDR='$NacosAddr';",
    "`$env:CARBON_RMQ_NAMESRV='$RocketMqNamesrv';",
    "`$env:CARBON_ES_URI='$EsUri';",
    "cd '$dir';",
    "& '$MavenCmd' -q -DskipTests spring-boot:run"
  ) -join " "

  Start-Process -FilePath "powershell" -ArgumentList @("-NoProfile", "-ExecutionPolicy", "Bypass", "-Command", $cmd) -RedirectStandardOutput $outFile -RedirectStandardError $errFile -WindowStyle Hidden
}

Write-Output "backend started"
