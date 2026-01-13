$Url = "https://archive.apache.org/dist/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.zip"
$ZipFile = "$PWD\apache-maven-3.8.8-bin.zip"
$ExtractPath = "$PWD"

Write-Host "Downloading Maven..."
Invoke-WebRequest -Uri $Url -OutFile $ZipFile

Write-Host "Extracting Maven..."
Expand-Archive -Path $ZipFile -DestinationPath $ExtractPath -Force

$MavenBin = Join-Path $ExtractPath "apache-maven-3.8.8\bin"
$env:Path += ";$MavenBin"

Write-Host "Maven installed to $MavenBin"
mvn -v
