param(
  [string]$ProjectDir = "$PSScriptRoot\..",
  [string]$NodeOptions = "--openssl-legacy-provider",
  [string]$WeChatQrUrl = ""
)

$ErrorActionPreference = "Stop"

Push-Location $ProjectDir
try {
  $env:NODE_OPTIONS = $NodeOptions
  if ($WeChatQrUrl) { $env:VUE_APP_WECHAT_QR_URL = $WeChatQrUrl }
  npm install
  npm run dev
} finally {
  Pop-Location
}
