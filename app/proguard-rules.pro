# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Media3 UI layouts reference drawables only from XML.
-keep class androidx.media3.ui.** { *; }
-keep class androidx.media3.ui.R$drawable { *; }
-keep class androidx.media3.ui.R$layout { *; }

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keepclasseswithmembers class * {
    @dagger.* <methods>;
    @javax.inject.* <fields>;
}

# Gson — persisted mitzvah JSON
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.beardytop.mitzmode.data.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Embedded Be a Tzaddik KMP module
-keep class com.beardytop.beatzaddik.** { *; }

# Sentry
-keepattributes LineNumberTable,SourceFile
-keep class io.sentry.** { *; }
-dontwarn io.sentry.**

# JavaMail (reflection-heavy)
-keep class com.sun.mail.** { *; }
-dontwarn com.sun.mail.**
