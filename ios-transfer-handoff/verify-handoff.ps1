# Fails if ios-transfer-handoff mirrors drifted from repo (be-a-tzaddik + android app).

$ErrorActionPreference = "Stop"
$repoRoot = Split-Path $PSScriptRoot -Parent
$src = Join-Path $repoRoot "be-a-tzaddik"
$dst = Join-Path $PSScriptRoot "be-a-tzaddik"
$androidSrc = Join-Path $repoRoot "app"
$androidDst = Join-Path $PSScriptRoot "android-mitzmode\app"

if (-not (Test-Path $src)) { Write-Error "Source not found: $src" }
if (-not (Test-Path $dst)) {
    Write-Error "Handoff mirror missing: $dst - run sync-to-ios-handoff.ps1 first"
}
if (-not (Test-Path $androidDst)) {
    Write-Error "Android mirror missing: $androidDst - run sync-to-ios-handoff.ps1 first"
}

$excludeDirs = @(
    "build", ".gradle", ".kotlin", ".idea", "node_modules",
    "Pods", "DerivedData", ".cxx", "captures",
    "androidApp", "tools"
)

function Get-RelativeFiles([string]$root) {
    Get-ChildItem -LiteralPath $root -Recurse -File |
        Where-Object {
            $rel = $_.FullName.Substring($root.Length + 1)
            $parts = $rel -split '\\'
            -not ($excludeDirs | Where-Object { $parts -contains $_ })
        } |
        ForEach-Object { $_.FullName.Substring($root.Length + 1).Replace('\', '/') }
}

$srcFiles = @(Get-RelativeFiles $src | Sort-Object)
$dstFiles = @(Get-RelativeFiles $dst | Sort-Object)

$onlySrc = Compare-Object $dstFiles $srcFiles | Where-Object SideIndicator -eq '=>' | ForEach-Object InputObject
$onlyDst = Compare-Object $dstFiles $srcFiles | Where-Object SideIndicator -eq '<=' | ForEach-Object InputObject

if ($onlySrc -or $onlyDst) {
    Write-Host "FILE LIST MISMATCH"
    if ($onlySrc) {
        Write-Host "`nIn source only ($($onlySrc.Count)):"
        $onlySrc | Select-Object -First 20 | ForEach-Object { Write-Host "  + $_" }
    }
    if ($onlyDst) {
        Write-Host "`nIn handoff only ($($onlyDst.Count)):"
        $onlyDst | Select-Object -First 20 | ForEach-Object { Write-Host "  - $_" }
    }
    exit 1
}

$mismatches = @()
foreach ($rel in $srcFiles) {
    $a = Join-Path $src ($rel -replace '/', '\')
    $b = Join-Path $dst ($rel -replace '/', '\')
    $ha = (Get-FileHash -LiteralPath $a).Hash
    $hb = (Get-FileHash -LiteralPath $b).Hash
    if ($ha -ne $hb) { $mismatches += $rel }
}

if ($mismatches.Count -gt 0) {
    Write-Host "CONTENT MISMATCH ($($mismatches.Count) files) - run sync-to-ios-handoff.ps1"
    $mismatches | Select-Object -First 30 | ForEach-Object { Write-Host "  $_" }
    if ($mismatches.Count -gt 30) { Write-Host "  ... and $($mismatches.Count - 30) more" }
    exit 1
}

Write-Host "OK: handoff mirror matches be-a-tzaddik ($($srcFiles.Count) files)"

$swiftManifest = Join-Path $PSScriptRoot "swift-native\MANIFEST.json"
if (-not (Test-Path $swiftManifest)) {
    Write-Error "Missing swift-native/MANIFEST.json - run sync-to-ios-handoff.ps1"
}
$manifest = Get-Content $swiftManifest -Raw | ConvertFrom-Json
foreach ($lang in @("he", "es", "fr", "ru")) {
    $rel = "BundledTranslations/$lang.json"
    $srcTr = Join-Path $src "shared\src\commonMain\composeResources\files\translations\$lang.json"
    $swiftTr = Join-Path $PSScriptRoot "swift-native\$rel"
    if ((Get-FileHash $srcTr).Hash -ne (Get-FileHash $swiftTr).Hash) {
        Write-Error "swift-native $rel out of sync with be-a-tzaddik"
    }
}
$fileCount = @($manifest.files.PSObject.Properties).Count
Write-Host "OK: swift-native translations match source ($fileCount exported files)"

# Android app mirror (source only; videos excluded by design)
$androidKtMismatches = @()
Get-ChildItem -LiteralPath $androidSrc -Recurse -File -Include *.kt,*.kts,*.xml,*.json,*.gradle,*.pro,*.properties |
    Where-Object {
        $rel = $_.FullName.Substring($androidSrc.Length + 1)
        if ($rel -match '^build\\') { return $false }
        if ($rel -match '\\assets\\.*\.(mp4|webm|mov|avi)$') { return $false }
        if ($rel -eq 'src\main\assets\starbg.png') { return $false }
        $true
    } |
    ForEach-Object {
        $rel = $_.FullName.Substring($androidSrc.Length + 1)
        $handoffFile = Join-Path $androidDst $rel
        if (-not (Test-Path $handoffFile)) {
            $androidKtMismatches += "+ missing $rel"
            return
        }
        if ((Get-FileHash -LiteralPath $_.FullName).Hash -ne (Get-FileHash -LiteralPath $handoffFile).Hash) {
            $androidKtMismatches += $rel
        }
    }
if ($androidKtMismatches.Count -gt 0) {
    Write-Host "ANDROID APP MISMATCH ($($androidKtMismatches.Count) files) - run sync-to-ios-handoff.ps1"
    $androidKtMismatches | Select-Object -First 20 | ForEach-Object { Write-Host "  $_" }
    exit 1
}
Write-Host "OK: android-mitzmode/app matches repo app/ (code + JSON; videos excluded)"

exit 0
