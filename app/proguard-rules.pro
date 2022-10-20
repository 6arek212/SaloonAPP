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

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


 -dontwarn retrofit.**
 -keep class retrofit.** { *; }
 -keepattributes Signature
 -keepattributes Exceptions



  -keepattributes Signature
  -keepattributes *Annotation*
  -keep class okhttp3.** { *; }
  -keep interface okhttp3.** { *; }
  -dontwarn okhttp3.**
  -dontwarn java.nio.file.*


# GSON
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer



 -keep class com.example.ibrasaloonapp.network.ApiResult


# Models
-keepclassmembers class com.example.ibrasaloonapp.domain.model.** {*;}
-keepclassmembers class com.example.ibrasaloonapp.network.model.** {*;}
-keepclassmembers class com.example.ibrasaloonapp.network.responses.** {*;}