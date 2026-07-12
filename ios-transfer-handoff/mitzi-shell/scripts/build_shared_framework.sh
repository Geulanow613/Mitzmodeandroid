#!/bin/sh
set -eu

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
KMP_ROOT="$ROOT/be-a-tzaddik"
MITZI_ROOT="$ROOT/mitzi"

if [ -d "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home" ]; then
  export JAVA_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
elif [ -d "/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home" ]; then
  export JAVA_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
fi

if [ -n "${JAVA_HOME:-}" ]; then
  export PATH="$JAVA_HOME/bin:$PATH"
fi

if ! command -v java >/dev/null 2>&1; then
  echo "error: Java 17+ is required. Install with: brew install openjdk@17" >&2
  exit 1
fi

echo "Building shared Kotlin framework..."
(cd "$KMP_ROOT" && ./gradlew :shared:podInstall :shared:syncFramework \
  -Pkotlin.native.cocoapods.platform=iphonesimulator \
  -Pkotlin.native.cocoapods.archs=arm64 \
  -Pkotlin.native.cocoapods.configuration=Debug)

echo "Installing CocoaPods..."
(cd "$MITZI_ROOT" && pod install)

echo "Done. Open MitzModeTest.xcworkspace in Xcode (not .xcodeproj)."
