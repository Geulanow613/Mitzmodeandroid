# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# Media3 UI layouts reference drawables only from XML; keep them if minify/shrink is enabled later.
-keep class androidx.media3.ui.** { *; }
-keepresourcexmlelements layout/exo_player_control_view
-keepresourcexmlelements layout/exo_player_view
-keep class androidx.media3.ui.R$drawable { *; }
-keep class androidx.media3.ui.R$layout { *; }