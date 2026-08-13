# Push mitzi Swift shell + be-a-tzaddik KMP to https://github.com/Geulanow613/mitzmodeios
# Secrets (Config.swift, Secrets.swift) are never included.
#
# Usage:
#   .\push-to-mitzmodeios.ps1
#   .\push-to-mitzmodeios.ps1 -Message "Custom commit message"

param(
    [string]$Message = ""
)

$ErrorActionPreference = "Stop"
$repoRoot = Split-Path $PSScriptRoot -Parent
$iosRepo = Join-Path $repoRoot "_mitzmodeios-repo"
$kmpSrc = Join-Path $repoRoot "be-a-tzaddik"
$mitziSrc = Join-Path $PSScriptRoot "mitzi-shell"

if (-not (Test-Path $iosRepo)) {
    Write-Host "Cloning mitzmodeios..."
    git clone https://github.com/Geulanow613/mitzmodeios.git $iosRepo
}

Write-Host "Refreshing mitzi-shell from Dropbox..."
& (Join-Path $PSScriptRoot "sync-mitzi-to-handoff.ps1")
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

$kmpExclude = @("build", ".gradle", ".kotlin", ".idea", "node_modules", "Pods", "DerivedData", ".cxx", "captures", "androidApp", "tools")
$mitziExclude = @("Pods", "build", "DerivedData", ".git", "be-a-tzaddik", "TempIcons", "xcuserdata", ".swiftpm")
$mitziExcludeFiles = @("*.mp4", "*.webm", "*.mov", "*.avi", "Config.swift", "Secrets.swift")

Write-Host "Updating be-a-tzaddik in mitzmodeios repo..."
$kmpDst = Join-Path $iosRepo "be-a-tzaddik"
if (Test-Path $kmpDst) { Remove-Item -LiteralPath $kmpDst -Recurse -Force }
New-Item -ItemType Directory -Path $kmpDst -Force | Out-Null
robocopy $kmpSrc $kmpDst /E /XD $kmpExclude /NFL /NDL /NJH /NJS /NC /NS | Out-Null
if ($LASTEXITCODE -ge 8) { Write-Error "robocopy be-a-tzaddik failed: $LASTEXITCODE" }

Write-Host "Updating mitzi in mitzmodeios repo..."
$mitziDst = Join-Path $iosRepo "mitzi"
if (Test-Path $mitziDst) { Remove-Item -LiteralPath $mitziDst -Recurse -Force }
New-Item -ItemType Directory -Path $mitziDst -Force | Out-Null
robocopy $mitziSrc $mitziDst /E /XD $mitziExclude /XF $mitziExcludeFiles /NFL /NDL /NJH /NJS /NC /NS | Out-Null
if ($LASTEXITCODE -ge 8) { Write-Error "robocopy mitzi failed: $LASTEXITCODE" }

# Ensure template exists (Config.swift stays local-only)
$template = Join-Path $mitziDst "Utilities\Config.template.swift"
if (-not (Test-Path $template)) {
    @"
enum Config {
    static let githubToken = "YOUR_GITHUB_TOKEN"
}
"@ | Set-Content -LiteralPath $template -Encoding UTF8
}

Push-Location $iosRepo
git fetch origin
git checkout main
git pull --ff-only origin main

git add -A
$status = git status --porcelain
if (-not $status) {
    Write-Host "Nothing to commit - mitzmodeios already up to date."
    Pop-Location
    exit 0
}

if (-not $Message) {
    $Message = @"
Sync mitzi Swift shell and be-a-tzaddik KMP checklist module.

Includes halachic text fixes, ChecklistEmbedBridge counter wiring, and iOS workflow updates. Config.swift and Secrets.swift are gitignored.
"@
}

git commit -m $Message
git push origin main
Pop-Location

Write-Host "Pushed to https://github.com/Geulanow613/mitzmodeios"
