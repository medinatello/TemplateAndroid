# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Jetpack Compose specific rules
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }

# Keep Compose runtime classes
-keepclassmembers class androidx.compose.runtime.** { *; }
-keep class androidx.compose.material3.** { *; }

# Keep custom composables and screens  
-keep class com.sortisplus.templateandroid.** { *; }
-keep class com.sortisplus.feature.** { *; }
-keep class com.sortisplus.core.** { *; }

# Preserve line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Preserve parameter names for better debugging
-keepattributes Signature,InnerClasses,EnclosingMethod

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Hide the original source file name
-renamesourcefileattribute SourceFile