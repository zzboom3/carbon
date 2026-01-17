param(
  [string]$ProjectDir = "$PSScriptRoot\..",
  [string]$NodeOptions = "--openssl-legacy-provider"
)

$ErrorActionPreference = "Stop"

Push-Location $ProjectDir
try {
  $env:NODE_OPTIONS = $NodeOptions
  npm install
  npm run dev
} finally {
  Pop-Location
}
