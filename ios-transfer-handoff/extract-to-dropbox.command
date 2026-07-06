#!/bin/bash
cd "$(dirname "$0")"
chmod +x extract-to-dropbox.sh 2>/dev/null || true
./extract-to-dropbox.sh
echo ""
read -n 1 -s -r -p "Press any key to close…"
