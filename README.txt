(1) Build the android_api_life.txt
(2) Build the apilife.model (based on the list of apis identified in step (1)).




<android.widget.GridView: void setAdapter(android.widget.ListAdapter)>:[1,10]

00011C10507671BC0E38A7161E6426717FED562E86DF54F35FCDFD6C5414B2B6
Speech Record crashes

Generic Programming
<?> removement
K, V expansion

K, V, E

<android.os.Build$VERSION: int SDK_INT>
=> If
=> Lookupswitch


OO for call graph construction
experimentally understand the difference between interface invocaton or virutal invocation
to extend (expand) the CFG.

Conditional Call Graph (CCG)
Polymorphic
Circulation


Group Truth

No Build.SDK_INT used.
Graddle tells that the methods cannot be resolved at the recent level


Catch Error ? ? ?


<android.app.Activity: void setPersistent(boolean)>:[1,10]
Not exist


/Volumes/joey/workspace/mudflow_benign_apps/0088969C4F4B03A537A257FD43B1C8552372724AD410B6A477AC48CB881AB51E.apk

0088969C4F4B03A537A257FD43B1C8552372724AD410B6A477AC48CB881AB51E-me.android.browser.log:// Short Msg: java.lang.NoSuchMethodError
0088969C4F4B03A537A257FD43B1C8552372724AD410B6A477AC48CB881AB51E-me.android.browser.log:// Long Msg: java.lang.NoSuchMethodError: 
No virtual method setPageCacheCapacity(I)V in class Landroid/webkit/WebSettings; or its super classes (declaration of 'android.webkit.WebSettings' appears in /system/framework/framework.jar:classes2.dex)
0088969C4F4B03A537A257FD43B1C8552372724AD410B6A477AC48CB881AB51E-me.android.browser.log:// 
java.lang.NoSuchMethodError: No virtual method setPageCacheCapacity(I)V in class Landroid/webkit/WebSettings; 
or its super classes (declaration of 'android.webkit.WebSettings' appears in /system/framework/framework.jar:classes2.dex)

05E6774A790FCC8564392B06ED9DFA83511EF5E575916ECE0A0D17A0A08C335B-com.aviary.android.feather.log:// 
Abort message: 'art/runtime/java_vm_ext.cc:470] 
JNI DETECTED ERROR IN APPLICATION: 
JNI GetFieldID called with pending exception java.lang.NoSuchFieldError: 
no "I" field "mNativeBitmap" in class "Landroid/graphics/Bitmap;" or its superclasses'

0844BDE4536346655CE87533E23E6F6B7BD1CE016B2042AFFC141117A1EEB3F9-uk.co.aifactory.backgammonfree.log:
// Abort message: 'art/runtime/java_vm_ext.cc:470] 
JNI DETECTED ERROR IN APPLICATION: JNI NewGlobalRef 
called with pending exception 
java.lang.NoSuchMethodError: 
no static or non-static method "Luk/co/aifactory/backgammonfree/BackgammonGridView;.eng_initNewGame(IZ)Z"'


The Apache HTTP client was deprecated in API 22 and removed in API 23. In this case it appears that they only removed it from the stub library, so apps using it will still run on Android M. You just can't compile them for Android M.

API behaviour may be changed.

It's worth mentioning that sometimes a method may become "undeprecated" (if that is a real word). For example, the WebSettings setLayoutAlgorithm method was deprecated in 4.0.1, then later reinstated in 4.0.3 just with one of the algorithms deprecated.

https://github.com/OneBusAway/onebusaway-android/issues/290


<android.content.res.AssetManager.AssetInputStream: int getAssetInt()>:[1,19]
Revert "Hide AssetInputStream.getAssetInt."
https://gitlab.tubit.tu-berlin.de/justus.beyer/streamagame_platform_frameworks_base/commit/b1bd1fe7fd9ed6b6e4518713ef5f5716a84d97e8



    am f61add84: am 2000fe58: Merge "Make getAssetInt throw unconditionally."
      Make getAssetInt throw unconditionally.
    am a393735a: am 2589067d: am 9fc8e6fe: am f61add84: am 2000fe58: Merge "Make getAssetInt throw unconditionally."
      Make getAssetInt throw unconditionally.
    am 2589067d: am 9fc8e6fe: am f61add84: am 2000fe58: Merge "Make getAssetInt throw unconditionally."
      Make getAssetInt throw unconditionally.
    am 9fc8e6fe: am f61add84: am 2000fe58: Merge "Make getAssetInt throw unconditionally."
      Make getAssetInt throw unconditionally.
    am f61add84: am 2000fe58: Merge "Make getAssetInt throw unconditionally."
      Make getAssetInt throw unconditionally.
    am 2000fe58: Merge "Make getAssetInt throw unconditionally."
      Make getAssetInt throw unconditionally.
    Merge "Make getAssetInt throw unconditionally."
    Make getAssetInt throw unconditionally.
    [x] Font and FileA3D use getNativeAsset instead of getAssetInt
        to get Asset Handles. The getAssetInt method will be
    am 2ba4efff: Hide AssetInputStream.getAssetInt (Take 2)
      Hide AssetInputStream.getAssetInt (Take 2)
    Hide AssetInputStream.getAssetInt (Take 2)
    This function will eventually replace getAssetInt,
    once all the classes that use getAssetInt are changed
    am 3a248a14: am ec70f809: am d34950cf: am 80b0496a: am 2db26788: am e71afc2e: Merge "Revert "Hide AssetInputStream.getAssetInt.""
      Revert "Hide AssetInputStream.getAssetInt."
    am ec70f809: am d34950cf: am 80b0496a: am 2db26788: am e71afc2e: Merge "Revert "Hide AssetInputStream.getAssetInt.""
      Revert "Hide AssetInputStream.getAssetInt."
    am d34950cf: am 80b0496a: am 2db26788: am e71afc2e: Merge "Revert "Hide AssetInputStream.getAssetInt.""
      Revert "Hide AssetInputStream.getAssetInt."
    am 80b0496a: am 2db26788: am e71afc2e: Merge "Revert "Hide AssetInputStream.getAssetInt.""
      Revert "Hide AssetInputStream.getAssetInt."
    am 2db26788: am e71afc2e: Merge "Revert "Hide AssetInputStream.getAssetInt.""
      Revert "Hide AssetInputStream.getAssetInt."
    am e71afc2e: Merge "Revert "Hide AssetInputStream.getAssetInt.""
      Revert "Hide AssetInputStream.getAssetInt."
    Merge "Revert "Hide AssetInputStream.getAssetInt.""
    Revert "Hide AssetInputStream.getAssetInt."
    am bc448267: am cce8e8a5: am 7f4dd912: am 271b28ac: am 80213160: am ac8ccc3c: Merge "Hide AssetInputStream.getAssetInt."
      Hide AssetInputStream.getAssetInt.
    am cce8e8a5: am 7f4dd912: am 271b28ac: am 80213160: am ac8ccc3c: Merge "Hide AssetInputStream.getAssetInt."
      Hide AssetInputStream.getAssetInt.
    am 7f4dd912: am 271b28ac: am 80213160: am ac8ccc3c: Merge "Hide AssetInputStream.getAssetInt."
      Hide AssetInputStream.getAssetInt.
    am 271b28ac: am 80213160: am ac8ccc3c: Merge "Hide AssetInputStream.getAssetInt."
      Hide AssetInputStream.getAssetInt.
    am 80213160: am ac8ccc3c: Merge "Hide AssetInputStream.getAssetInt."
      Hide AssetInputStream.getAssetInt.
    am ac8ccc3c: Merge "Hide AssetInputStream.getAssetInt."
      Hide AssetInputStream.getAssetInt.
    Merge "Hide AssetInputStream.getAssetInt."
    am f8f09a15: Hide AssetInputStream.getAssetInt.
      Hide AssetInputStream.getAssetInt.
    Hide AssetInputStream.getAssetInt.
    Hide AssetInputStream.getAssetInt.

Make getAssetInt throw unconditionally.
Revert "Hide AssetInputStream.getAssetInt.
Hide AssetInputStream.getAssetInt.  ==>commit f8f09a15a409f373f22aa475bb0defd264088e4f, Jan 10



gloomy-dungeons-3d
Backward Compatibility issue


com.github.yeriomin.workoutlog_1-com.github.yeriomin.workoutlog.log:// Short Msg: java.lang.NoSuchFieldException
com.github.yeriomin.workoutlog_1-com.github.yeriomin.workoutlog.log:// Long Msg: java.lang.NoSuchFieldException: TABLENAME
com.github.yeriomin.workoutlog_1-com.github.yeriomin.workoutlog.log:// Caused by: java.lang.NoSuchFieldException: TABLENAME
com.iven.lfflfeedreader_56-com.iven.lfflfeedreader.log:// Short Msg: java.lang.NoSuchMethodError
com.iven.lfflfeedreader_56-com.iven.lfflfeedreader.log:// Long Msg: java.lang.NoSuchMethodError: android.view.KeyEvent.hasModifiers
com.iven.lfflfeedreader_56-com.iven.lfflfeedreader.log:// java.lang.NoSuchMethodError: android.view.KeyEvent.hasModifiers
com.vuze.android.remote_63-com.vuze.android.remote.log:// Short Msg: java.lang.NoSuchMethodError
com.vuze.android.remote_63-com.vuze.android.remote.log:// Long Msg: java.lang.NoSuchMethodError: a.a.getDrawable
com.vuze.android.remote_63-com.vuze.android.remote.log:// Caused by: java.lang.NoSuchMethodError: a.a.getDrawable
com.yasfa.views_3-com.yasfa.views.log:// Short Msg: java.lang.NoSuchMethodError
com.yasfa.views_3-com.yasfa.views.log:// Long Msg: java.lang.NoSuchMethodError: com.yasfa.views.FButton.setAlpha
com.yasfa.views_3-com.yasfa.views.log:// java.lang.NoSuchMethodError: com.yasfa.views.FButton.setAlpha
de.danoeh.antennapod_1050102-de.danoeh.antennapod.log:// Short Msg: java.lang.NoSuchMethodError
de.danoeh.antennapod_1050102-de.danoeh.antennapod.log:// Long Msg: java.lang.NoSuchMethodError: android.view.KeyEvent.hasModifiers
de.danoeh.antennapod_1050102-de.danoeh.antennapod.log:// java.lang.NoSuchMethodError: android.view.KeyEvent.hasModifiers
org.ligi.gobandroid_hd_225-org.ligi.gobandroid_hd.log:// Short Msg: java.lang.NoSuchMethodError
org.ligi.gobandroid_hd_225-org.ligi.gobandroid_hd.log:// Long Msg: java.lang.NoSuchMethodError: android.view.KeyEvent.hasModifiers
org.ligi.gobandroid_hd_225-org.ligi.gobandroid_hd.log:// java.lang.NoSuchMethodError: android.view.KeyEvent.hasModifiers
org.ligi.passandroid_272-org.ligi.passandroid.log:// Short Msg: java.lang.NoSuchMethodError
org.ligi.passandroid_272-org.ligi.passandroid.log:// Long Msg: java.lang.NoSuchMethodError: android.view.KeyEvent.hasModifiers
org.ligi.passandroid_272-org.ligi.passandroid.log:// java.lang.NoSuchMethodError: android.view.KeyEvent.hasModifiers
org.vi_server.red_screen_2-org.vi_server.red_screen.log:// Short Msg: java.lang.NoSuchMethodError
org.vi_server.red_screen_2-org.vi_server.red_screen.log:// Long Msg: java.lang.NoSuchMethodError: android.view.View.setSystemUiVisibility
org.vi_server.red_screen_2-org.vi_server.red_screen.log:// java.lang.NoSuchMethodError: android.view.View.setSystemUiVisibility




FicFinder: False Alarms

openDatabase

Added in API level 1
SQLiteDatabase openDatabase (String path, 
                SQLiteDatabase.CursorFactory factory, 
                int flags)
Open the database according to the flags OPEN_READWRITE OPEN_READONLY CREATE_IF_NECESSARY and/or NO_LOCALIZED_COLLATORS.

Sets the locale of the database to the the system's current locale. Call setLocale(Locale) if you would like something else.
