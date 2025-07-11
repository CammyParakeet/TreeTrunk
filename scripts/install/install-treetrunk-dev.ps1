# install-treetrunk-dev.ps1
$installDir = "$env:USERPROFILE\.treetrunk"
$jarUrl = "TODO"
$localJarSrc = "..\cli\build\libs\treetrunk-cli-all.jar"
$jarPath = "$installDir\treetrunk-cli.jar"
$batPath = "$installDir\treetrunk.bat"

# Create install directory
if (-Not (Test-Path $installDir)) {
    New-Item -ItemType Directory -Force -Path $installDir | Out-Null
}

# Download latest Jar
# TODO

# Copy local jar
if (-Not (Test-Path $localJarSrc)) {
    Write-Host "Cannot find local JAR at $localJarSrc. "
    exit 1
}

Copy-Item -Path $localJarSrc -Destination $jarPath -Force
Write-Host "Copied JAR to $jarPath"

# Write batch wrapper
$batComponent = '@echo off
java -jar "%~dp0treetrunk-cli.jar" %*'
Set-Content -Path $batPath -Value $batComponent -Encoding Ascii

# Add to PATH
$envPath = [Environment]::GetEnvironmentVariable("PATH", "User")
if ([string]::IsNullOrWhiteSpace($envPath)) {
    $envPath=""
}

# Check if already added
if ($envPath -split ";" -contains $installDir) {
    Write-Host "$installDir is already in the user PATH."
    $addedToPath = $false
} else {
    $newPath = "$envPath;$installDir"
    [Environment]::SetEnvironmentVariable("PATH", $newPath, "User")
    Write-Host "Added $installDir to user PATH."
    $addedToPath = $true
}

Write-Host "`nTreeTrunk installed locally to $installDir"
if ($addedToPath) {
    Write-Host "Please refresh terminal to use"
}
