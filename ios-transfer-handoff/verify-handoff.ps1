# Fails if ios-transfer-handoff/be-a-tzaddik drifted from repo be-a-tzaddik (excluding androidApp, tools, build dirs).

$ErrorActionPreference = "Stop"
$repoRoot = Split-Path $PSScriptRoot -Parent
$src = Join-Path $repoRoot "be-a-tzaddik"
$dst = Join-Path $PSScriptRoot "be-a-tzaddik"

if (-not (Test-Path $src)) { Write-Error "Source not found: $src" }
if (-not (Test-Path $dst)) {
    Write-Error "Handoff mirror missing: $dst - run sync-to-ios-handoff.ps1 first"
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
exit 0
