$ProgressPreference = 'SilentlyContinue'

function Invoke-Api([string]$method, [string]$url, [hashtable]$headers = $null, $body = $null) {
  try {
    $params = @{ Method = $method; Uri = $url; UseBasicParsing = $true }
    if ($headers) { $params.Headers = $headers }
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

$tests = @(
  @{ name = 'system swagger'; method='GET'; url='http://localhost:9002/system/v2/api-docs' },
  @{ name = 'assets swagger'; method='GET'; url='http://localhost:9003/assets/v2/api-docs' },

  @{ name = 'system H5 article page'; method='POST'; url='http://localhost:9002/system/carbonH5Article/getPageList'; body=@{ current=1; size=5 } },
  @{ name = 'system H5 search default'; method='POST'; url='http://localhost:9002/system/carbonH5Article/getByKeyWord/default'; body=@{ current=1; size=5; keyword='carbon' } },
  @{ name = 'system H5 search article'; method='POST'; url='http://localhost:9002/system/carbonH5Article/getByKeyWord'; body=@{ current=1; size=5; keyword='carbon'; categoryId='' } },
  @{ name = 'system H5 search methodology'; method='POST'; url='http://localhost:9002/system/carbonH5Article/getByKeyWord/methodology'; body=@{ current=1; size=5; keyword='carbon'; fieldCode=''; industryCode='' } },

  @{ name = 'assets metaregistry byRef'; method='GET'; url='http://localhost:9003/assets/EscarbonMetaregistry/getByRef?refId=__smoke__&typeCode=__smoke__' }
)

$failed = 0
foreach ($t in $tests) {
  $res = Invoke-Api $t.method $t.url $null $t.body
  $status = $res.status
  $ok = ($status -eq 200) -and ($res.body -notmatch '\"code\"\\s*:\\s*500')
  if ($ok) {
    "PASS $($t.name) => HTTP $status"
  } else {
    $failed++
    "FAIL $($t.name) => HTTP $status"
    $snippet = $res.body
    if ($snippet.Length -gt 300) { $snippet = $snippet.Substring(0,300) }
    $snippet
  }
}

if ($failed -gt 0) {
  throw "Smoke failed: $failed"
}
