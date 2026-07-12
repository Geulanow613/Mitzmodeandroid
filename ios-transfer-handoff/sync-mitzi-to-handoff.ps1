# Mirrors Mitz Mode iOS Swift shell from Dropbox into ios-transfer-handoff/mitzi-shell.
# Does NOT touch be-a-tzaddik (Kotlin) or Android app/.
#
# Usage:
#   .\sync-mitzi-to-handoff.ps1              # Dropbox mitzi -> handoff/mitzi-shell (default)
#   .\sync-mitzi-to-handoff.ps1 -PushToDropbox   # handoff/mitzi-shell -> Dropbox mitzi (restore)

param(
    [switch]$PushToDropbox
)

$ErrorActionPreference = "Stop"
$repoRoot = Split-Path $PSScriptRoot -Parent
$dropboxMitzi = Join-Path $repoRoot "Dropbox\claudesucks\mitzi"
$shellDst = Join-Path $PSScriptRoot "mitzi-shell"

$excludeDirs = @(
    "Pods", "build", "DerivedData", ".git", "be-a-tzaddik", "TempIcons",
    "xcuserdata", ".swiftpm"
)
$excludeFiles = @("*.mp4", "*.webm", "*.mov", "*.avi", "Config.swift", "Secrets.swift")

if ($PushToDropbox) {
    if (-not (Test-Path $shellDst)) {
        Write-Error "mitzi-shell not found: $shellDst (run without -PushToDropbox first)"
    }
    if (-not (Test-Path $dropboxMitzi)) {
        New-Item -ItemType Directory -Path $dropboxMitzi -Force | Out-Null
    }
    Write-Host "Push mitzi-shell -> Dropbox/claudesucks/mitzi (Pods and videos excluded)"
    robocopy $shellDst $dropboxMitzi /E /XD $excludeDirs /XF $excludeFiles /NFL /NDL /NJH /NJS /NC /NS | Out-Null
    if ($LASTEXITCODE -ge 8) {
        Write-Error "robocopy push failed with exit code $LASTEXITCODE"
    }
    Write-Host "Done. On Mac: cd mitzi && pod install"
    exit 0
}

if (-not (Test-Path $dropboxMitzi)) {
    Write-Error "Dropbox mitzi not found: $dropboxMitzi"
}

Write-Host "Mirror Dropbox/claudesucks/mitzi -> ios-transfer-handoff/mitzi-shell"
Write-Host "(Swift shell only - no Pods, no orphan be-a-tzaddik, no reward videos)"

if (Test-Path $shellDst) {
    Remove-Item -LiteralPath $shellDst -Recurse -Force
}
New-Item -ItemType Directory -Path $shellDst -Force | Out-Null

robocopy $dropboxMitzi $shellDst /E /XD $excludeDirs /XF $excludeFiles /NFL /NDL /NJH /NJS /NC /NS | Out-Null
if ($LASTEXITCODE -ge 8) {
    Write-Error "robocopy failed with exit code $LASTEXITCODE"
}

$keyFiles = @(
    "Podfile",
    "project.yml",
    "MitzvahApp.swift",
    "Views\Checklist\DailyChecklistHostView.swift",
    "ViewModels\DailyMitzvotViewModel+ChecklistCounter.swift",
    "CHECKLIST_COUNTER_INTEGRATION.md",
    "scripts\build_shared_framework.sh"
)
foreach ($rel in $keyFiles) {
    $path = Join-Path $shellDst $rel
    if (-not (Test-Path $path)) {
        Write-Warning "Expected mitzi file missing after sync: $rel"
    }
}

$generatedAt = (Get-Date).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
$meta = @{
    generated_at = $generatedAt
    source = "Dropbox/claudesucks/mitzi"
    file_count = (Get-ChildItem -LiteralPath $shellDst -Recurse -File).Count
    excludes = @{
        dirs = $excludeDirs
        files = $excludeFiles
    }
} | ConvertTo-Json -Depth 4
Set-Content -LiteralPath (Join-Path $shellDst "SYNC_MANIFEST.json") -Value $meta

Write-Host "Done. $($meta | ConvertFrom-Json | Select-Object -ExpandProperty file_count) files in mitzi-shell"
Write-Host "Commit ios-transfer-handoff/mitzi-shell/ to preserve iOS-specific changes in git."
exit 0
