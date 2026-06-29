# Mirrors iOS-relevant be-a-tzaddik sources into ios-transfer-handoff (file bundle for agents).
# Excludes androidApp, tools, and build artifacts.
#
# Before mirroring: copies be-a-tzaddik/data/*.json into composeResources/files/ so the
# bundled JSON the app loads matches the editable data/ copies (checklist explanations, etc.).

$ErrorActionPreference = "Stop"
$repoRoot = Split-Path $PSScriptRoot -Parent
$src = Join-Path $repoRoot "be-a-tzaddik"
$dst = Join-Path $PSScriptRoot "be-a-tzaddik"

if (-not (Test-Path $src)) {
    Write-Error "Source not found: $src"
}

$dataDir = Join-Path $src "data"
$resDir = Join-Path $src "shared\src\commonMain\composeResources\files"
$bundleJson = @(
    "checklist-items.json",
    "nusach-extras.json",
    "holidays-overlay.json",
    "manual-cities.json"
)

Write-Host "Step 1: data/ -> composeResources/files/ (iOS/Android bundle source)"
foreach ($name in $bundleJson) {
    $from = Join-Path $dataDir $name
    $to = Join-Path $resDir $name
    if (-not (Test-Path $from)) {
        Write-Warning "Skip (missing in data/): $name"
        continue
    }
    Copy-Item -LiteralPath $from -Destination $to -Force
    Write-Host "  $name"
}

$excludeDirs = @(
    "build", ".gradle", ".kotlin", ".idea", "node_modules",
    "Pods", "DerivedData", ".cxx", "captures",
    "androidApp", "tools"
)

Write-Host "Step 2: robocopy be-a-tzaddik -> ios-transfer-handoff/be-a-tzaddik"
Write-Host "(shared, iosApp, data, gradle - no androidApp/tools)"

if (Test-Path $dst) {
    Remove-Item -LiteralPath $dst -Recurse -Force
}
New-Item -ItemType Directory -Path $dst -Force | Out-Null

robocopy $src $dst /E /XD $excludeDirs /NFL /NDL /NJH /NJS /NC /NS | Out-Null
if ($LASTEXITCODE -ge 8) {
    Write-Error "robocopy failed with exit code $LASTEXITCODE"
}

Write-Host "Step 3: verify bundled checklist copy in handoff"
$handoffChecklist = Join-Path $dst "shared\src\commonMain\composeResources\files\checklist-items.json"
if (-not (Test-Path $handoffChecklist)) {
    Write-Error "Missing handoff checklist: $handoffChecklist"
}
$srcHash = (Get-FileHash (Join-Path $resDir "checklist-items.json")).Hash
$dstHash = (Get-FileHash $handoffChecklist).Hash
if ($srcHash -ne $dstHash) {
    Write-Error "checklist-items.json mismatch after sync"
}
Write-Host "  checklist-items.json OK ($srcHash)"

Write-Host "Step 4: verify bundled translation JSON in handoff"
$translationDir = Join-Path $resDir "translations"
$handoffTranslationDir = Join-Path $dst "shared\src\commonMain\composeResources\files\translations"
foreach ($lang in @("he", "es", "fr", "ru")) {
    $srcFile = Join-Path $translationDir "$lang.json"
    $dstFile = Join-Path $handoffTranslationDir "$lang.json"
    if (-not (Test-Path $srcFile)) {
        Write-Warning "Missing source translation bundle: $srcFile (run be-a-tzaddik/tools/compile_full_bundled.py first)"
        continue
    }
    if (-not (Test-Path $dstFile)) {
        Write-Error "Missing handoff translation bundle: $dstFile"
    }
    $srcLangHash = (Get-FileHash $srcFile).Hash
    $dstLangHash = (Get-FileHash $dstFile).Hash
    if ($srcLangHash -ne $dstLangHash) {
        Write-Error "$lang.json translation bundle mismatch after sync"
    }
    Write-Host "  translations/$lang.json OK"
}

Write-Host "Done. Agent entry point: ios-transfer-handoff/AGENTS.md"
