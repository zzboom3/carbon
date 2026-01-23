param(
  [string]$MySqlBin = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
  [string]$MySqldBin = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqld.exe",
  [string]$DefaultsFile = "C:\ProgramData\MySQL\MySQL Server 8.0\my.ini",
  [string]$ServiceName = "MySQL80",
  [string]$RootPassword = "root",
  [string]$DbName = "carbon",
  [string]$ResetSqlFile = "$PSScriptRoot\mysql-reset-root.sql",
  [string]$ImportSqlFile = "$PSScriptRoot\..\carbon.sql"
)

$ErrorActionPreference = "Stop"

$principal = [Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()
if (-not $principal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
  throw "Run this script in an elevated PowerShell (Administrator) to stop/start MySQL service and reset root password."
}

Stop-Service $ServiceName -Force

$p = Start-Process -FilePath $MySqldBin -ArgumentList @(
  "--defaults-file=$DefaultsFile",
  "--init-file=$ResetSqlFile",
  "--console"
) -PassThru

Start-Sleep -Seconds 6

if (-not $p.HasExited) {
  Stop-Process -Id $p.Id -Force
}

Start-Service $ServiceName

$oldMySqlPwd = $env:MYSQL_PWD
$env:MYSQL_PWD = $RootPassword
try {
  & $MySqlBin "-uroot" "-e" "CREATE DATABASE IF NOT EXISTS $DbName DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
  Get-Content -Path $ImportSqlFile -Raw | & $MySqlBin "-uroot" $DbName
} finally {
  $env:MYSQL_PWD = $oldMySqlPwd
}

Write-Output "mysql bootstrap done"
