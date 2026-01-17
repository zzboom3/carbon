param(
  [string]$Root = "$PSScriptRoot\..\.."
)

$ErrorActionPreference = "Stop"

$exclude = @("\.git\", "\target\", "\node_modules\", "\uploads\")
$patterns = @(
  "BEGIN (RSA|EC|OPENSSH) PRIVATE KEY",
  "PRIVATE KEY-----",
  "\.sign\.key",
  "\.tls\.key",
  "password\\s*[:=]",
  "AKIA[0-9A-Z]{16}",
  "secret(_key)?\\s*[:=]"
)

$files = Get-ChildItem -Path $Root -Recurse -File | Where-Object {
  $p = $_.FullName
  if ($p -eq $MyInvocation.MyCommand.Path) { return $false }
  foreach ($x in $exclude) { if ($p -like "*$x*") { return $false } }
  return $true
}

$hits = @()
foreach ($f in $files) {
  foreach ($pat in $patterns) {
    $m = Select-String -Path $f.FullName -Pattern $pat -SimpleMatch:$false -CaseSensitive:$false -ErrorAction SilentlyContinue
    if ($m) { $hits += $m }
  }
}

if ($hits.Count -gt 0) {
  $hits | Select-Object Path, LineNumber, Line | Format-Table -AutoSize
  throw "secret scan failed: $($hits.Count) hit(s)"
}

Write-Host "secret scan passed"
