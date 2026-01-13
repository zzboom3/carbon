$ProgressPreference = 'SilentlyContinue'

function Invoke-Api([string]$method, [string]$url, $body = $null) {
  try {
    $params = @{ Method = $method; Uri = $url; UseBasicParsing = $true }
    if ($body -ne $null) {
      $params.ContentType = 'application/json'
      $params.Body = ($body | ConvertTo-Json -Depth 20)
    }
    $r = Invoke-WebRequest @params
    return @{ ok = $true; status = $r.StatusCode; body = $r.Content }
  } catch {
    if ($_.Exception.Response) {
      $sr = New-Object IO.StreamReader($_.Exception.Response.GetResponseStream())
      $c = $sr.ReadToEnd()
      return @{ ok = $false; status = $_.Exception.Response.StatusCode.value__; body = $c }
    }
    return @{ ok = $false; status = -1; body = $_.Exception.Message }
  }
}

$base = 'http://localhost:9005/trade'
$tests = @(
  @{ name = 'trade swagger'; method='GET'; url="$base/v2/api-docs"; expect='"swagger"\s*:\s*"2\.0"' },
  @{ name = 'quote list buy'; method='POST'; url="$base/carbonTradeQuote/getPageList"; body=@{ current=1; size=5; tradeRole='0270000001' }; expect='"code"\s*:\s*200' },
  @{ name = 'quote list sell'; method='POST'; url="$base/carbonTradeQuote/getPageList"; body=@{ current=1; size=5; tradeRole='0270000002' }; expect='"code"\s*:\s*200' },
  @{ name = 'price list'; method='POST'; url="$base/carbonTradePrice/getPageList"; body=@{ current=1; size=5 }; expect='"code"\s*:\s*200' }
)

$failed = 0
foreach ($t in $tests) {
  $res = Invoke-Api $t.method $t.url $t.body
  $status = $res.status
  $ok = ($status -eq 200) -and ($res.body -match $t.expect)
  if ($ok) {
    "PASS $($t.name) => HTTP $status"
  } else {
    $failed++
    "FAIL $($t.name) => HTTP $status"
    $snippet = $res.body
    if ($snippet.Length -gt 400) { $snippet = $snippet.Substring(0,400) }
    $snippet
  }
}

if ($failed -gt 0) {
  throw "Smoke failed: $failed"
}
