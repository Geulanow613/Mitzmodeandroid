#!/usr/bin/env bash
# Extract ios-transfer-handoff to Dropbox/claudesucks (Mac/Linux).
# Double-click extract-to-dropbox.command on Mac, or run: ./extract-to-dropbox.sh

set -euo pipefail

HANDOFF_ROOT="$(cd "$(dirname "$0")" && pwd)"
DROPBOX=""

for candidate in \
    "$HOME/Library/CloudStorage/Dropbox" \
    "$HOME/Dropbox"
do
    if [[ -d "$candidate" ]]; then
        DROPBOX="$candidate"
        break
    fi
done

if [[ -z "$DROPBOX" ]]; then
    echo "ERROR: Dropbox folder not found."
    echo "Looked in:"
    echo "  ~/Library/CloudStorage/Dropbox"
    echo "  ~/Dropbox"
    echo "Install Dropbox or create one of those folders, then run again."
    exit 1
fi

DEST="$DROPBOX/claudesucks"
mkdir -p "$DEST"

echo "Handoff source: $HANDOFF_ROOT"
echo "Extracting to:  $DEST"
echo ""

RSYNC_EXCLUDES=(
    --exclude '.DS_Store'
    --exclude 'Thumbs.db'
    --exclude '.git'
)

if command -v rsync >/dev/null 2>&1; then
    rsync -a --delete "${RSYNC_EXCLUDES[@]}" "$HANDOFF_ROOT/" "$DEST/"
else
    echo "rsync not found — using cp (slower, no delete of stale files)"
    mkdir -p "$DEST"
    cp -R "$HANDOFF_ROOT"/. "$DEST/"
fi

echo ""
echo "Done. Files are in:"
echo "  $DEST"
echo ""
echo "Next steps:"
echo "  • KMP checklist:  $DEST/be-a-tzaddik/shared"
echo "  • Swift JSON:     $DEST/swift-native"
echo "  • Android app:    $DEST/android-mitzmode"
echo "  • Home gradient:  $DEST/docs/MITZ_MODE_HOME_BACKGROUND.md"
echo "  • Mac guide:      $DEST/MAC_QUICKSTART.md"
