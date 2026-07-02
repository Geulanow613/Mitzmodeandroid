# Mirrors iOS-relevant be-a-tzaddik sources + Android Mitz Mode app into ios-transfer-handoff.
# Excludes build artifacts, tools, and large media (videos).
#
# Before mirroring: exports swift-native JSON + copies data/*.json into composeResources/files/.
#
# Usage:
#   .\sync-to-ios-handoff.ps1              # full refresh (default)
#   .\sync-to-ios-handoff.ps1 -SkipSwiftExport   # robocopy only (swift-native already built)

param(
    [switch]$SkipSwiftExport
)

$ErrorActionPreference = "Stop"
$repoRoot = Split-Path $PSScriptRoot -Parent
$src = Join-Path $repoRoot "be-a-tzaddik"
$dst = Join-Path $PSScriptRoot "be-a-tzaddik"
$exportScript = Join-Path $src "tools\_export_ios_handoff.py"
$androidSrc = $repoRoot
$androidDst = Join-Path $PSScriptRoot "android-mitzmode"

if (-not (Test-Path $src)) {
    Write-Error "Source not found: $src"
}

if (-not $SkipSwiftExport) {
    Write-Host "Step 0: export swift-native + sync data/ -> composeResources (Python)"
    if (-not (Test-Path $exportScript)) {
        Write-Error "Missing export script: $exportScript"
    }
    & python $exportScript
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}

$resDir = Join-Path $src "shared\src\commonMain\composeResources\files"

$excludeDirs = @(
    "build", ".gradle", ".kotlin", ".idea", "node_modules",
    "Pods", "DerivedData", ".cxx", "captures",
    "androidApp", "tools"
)

Write-Host "Step 1: robocopy be-a-tzaddik -> ios-transfer-handoff/be-a-tzaddik"
Write-Host "(shared, iosApp, data, gradle - no androidApp/tools)"

if (Test-Path $dst) {
    Remove-Item -LiteralPath $dst -Recurse -Force
}
New-Item -ItemType Directory -Path $dst -Force | Out-Null

robocopy $src $dst /E /XD $excludeDirs /NFL /NDL /NJH /NJS /NC /NS | Out-Null
if ($LASTEXITCODE -ge 8) {
    Write-Error "robocopy failed with exit code $LASTEXITCODE"
}

Write-Host "Step 2: verify bundled checklist copy in handoff"
$handoffChecklist = Join-Path $dst "shared\src\commonMain\composeResources\files\checklist-items.json"
if (-not (Test-Path $handoffChecklist)) {
    Write-Error "Missing handoff checklist: $handoffChecklist"
}
$srcHash = (Get-FileHash (Join-Path $resDir "checklist-items.json")).Hash
$dstHash = (Get-FileHash $handoffChecklist).Hash
if ($srcHash -ne $dstHash) {
    Write-Error "checklist-items.json mismatch after sync"
}
Write-Host "  checklist-items.json OK"

Write-Host "Step 3: verify bundled translation JSON in handoff"
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

Write-Host "Step 4: verify swift-native export"
$swiftNative = Join-Path $PSScriptRoot "swift-native\MANIFEST.json"
if (-not (Test-Path $swiftNative)) {
    Write-Error "Missing swift-native/MANIFEST.json - run without -SkipSwiftExport"
}
Write-Host "  swift-native/MANIFEST.json OK"

Write-Host "Step 5: mirror Android Mitz Mode app -> ios-transfer-handoff/android-mitzmode"
$androidExcludeDirs = @("build", ".gradle", ".kotlin", ".idea", "captures", ".cxx")
$androidExcludeFiles = @("*.mp4", "*.webm", "*.mov", "*.avi", "starbg.png")

if (Test-Path (Join-Path $androidDst "app")) {
    Remove-Item -LiteralPath (Join-Path $androidDst "app") -Recurse -Force
}

$gradleItems = @(
    "settings.gradle",
    "build.gradle.kts",
    "build.gradle",
    "gradle.properties",
    "gradlew",
    "gradlew.bat"
)
foreach ($item in $gradleItems) {
    $from = Join-Path $androidSrc $item
    if (Test-Path $from) {
        Copy-Item -LiteralPath $from -Destination (Join-Path $androidDst $item) -Force
    }
}

$gradleWrapperSrc = Join-Path $androidSrc "gradle"
$gradleWrapperDst = Join-Path $androidDst "gradle"
if (Test-Path $gradleWrapperDst) {
    Remove-Item -LiteralPath $gradleWrapperDst -Recurse -Force
}
if (Test-Path $gradleWrapperSrc) {
    robocopy $gradleWrapperSrc $gradleWrapperDst /E /NFL /NDL /NJH /NJS /NC /NS | Out-Null
    if ($LASTEXITCODE -ge 8) {
        Write-Error "robocopy gradle/ failed with exit code $LASTEXITCODE"
    }
}

$appSrc = Join-Path $androidSrc "app"
$appDst = Join-Path $androidDst "app"
robocopy $appSrc $appDst /E /XD $androidExcludeDirs /XF $androidExcludeFiles /NFL /NDL /NJH /NJS /NC /NS | Out-Null
if ($LASTEXITCODE -ge 8) {
    Write-Error "robocopy app/ failed with exit code $LASTEXITCODE"
}

# Point shared module at sibling be-a-tzaddik in handoff layout.
$settingsPath = Join-Path $androidDst "settings.gradle"
if (Test-Path $settingsPath) {
    $settingsText = Get-Content $settingsPath -Raw
    $settingsText = $settingsText -replace "file\('be-a-tzaddik/shared'\)", "file('../be-a-tzaddik/shared')"
    Set-Content -LiteralPath $settingsPath -Value $settingsText -NoNewline
}

$appModule = Join-Path $appDst "src\main\assets\mitzvotlistfull.json"
if (-not (Test-Path $appModule)) {
    Write-Error "Missing app assets mitzvotlistfull.json after android mirror"
}
Write-Host "  android-mitzmode/app OK (videos excluded)"

$generatedAt = (Get-Date).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
$syncMeta = @{
    generated_at = $generatedAt
    be_a_tzaddik_files = (Get-ChildItem -LiteralPath $dst -Recurse -File).Count
    android_app_files = (Get-ChildItem -LiteralPath $appDst -Recurse -File).Count
    swift_native_manifest = "swift-native/MANIFEST.json"
    extract_mac = "extract-to-dropbox.command"
    dropbox_dest = "~/Dropbox/claudesucks (or ~/Library/CloudStorage/Dropbox/claudesucks)"
} | ConvertTo-Json -Depth 3
Set-Content -LiteralPath (Join-Path $PSScriptRoot "SYNC_MANIFEST.json") -Value $syncMeta

Write-Host "Done. Mac: double-click extract-to-dropbox.command"
Write-Host "Agent entry point: ios-transfer-handoff/AGENTS.md"
