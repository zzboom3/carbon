param(
  [string]$MavenCmd = "$PSScriptRoot\..\apache-maven-3.8.8\bin\mvn.cmd"
)

$ErrorActionPreference = "Stop"

function Invoke-Mvn([string]$ProjectDir, [string[]]$Args) {
  Push-Location $ProjectDir
  try {
    & $MavenCmd @Args
  } finally {
    Pop-Location
  }
}

Invoke-Mvn "$PSScriptRoot\..\carbon-frame\carbon-common-model" @("-DskipTests", "compile")
Invoke-Mvn "$PSScriptRoot\..\carbon-assets" @("-DskipTests", "-Ddocker.skip=true", "compile")
Invoke-Mvn "$PSScriptRoot\..\carbon-trade" @("-DskipTests", "-Ddocker.skip=true", "compile")
Invoke-Mvn "$PSScriptRoot\..\carbon-auth" @("-DskipTests", "compile")
Invoke-Mvn "$PSScriptRoot\..\carbon-system" @("-DskipTests", "compile")
Invoke-Mvn "$PSScriptRoot\..\carbon-gate" @("-DskipTests", "compile")
Invoke-Mvn "$PSScriptRoot\..\carbon-chainmaker" @("-DskipTests", "compile")

Invoke-Mvn "$PSScriptRoot\..\carbon-assets" @("-DskipTests", "-Ddocker.skip=true", "install")
Invoke-Mvn "$PSScriptRoot\..\carbon-mq" @("-DskipTests", "-Ddocker.skip=true", "compile")
