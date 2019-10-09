# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/elek/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Crashlytics configuration
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Retain constructor that is called by using reflection to recreate the Controller
-keepclassmembers public class * extends com.bluelinelabs.conductor.Controller {
   public <init>();
   public <init>(android.os.Bundle);
}
-keepclassmembers public class * extends com.bluelinelabs.conductor.ControllerChangeHandler {
   public <init>();
}

-keepclassmembers class * implements android.os.Parcelable {
    static *** CREATOR;
}
