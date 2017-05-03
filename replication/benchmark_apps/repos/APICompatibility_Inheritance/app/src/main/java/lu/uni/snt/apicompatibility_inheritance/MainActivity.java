package lu.uni.snt.apicompatibility_inheritance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.transition.TransitionManager;

/**
 * getContentTransitionManager() is introduced at API level 21, and
 * go() is introduced at API level 19.
 * The current minSdkVersion is set to 10.
 * Therefore, this app will crash on devices running a platform with an API version smaller than 21.
 *
 * E/AndroidRuntime: FATAL EXCEPTION: main
 *                   java.lang.NoSuchMethodError: android.app.Activity.getContentTransitionManager
 *
 * This app contains two API compatibility issues:
 * 1) <android.app.Activity: android.transition.TransitionManager getContentTransitionManager()>:[21,25]
 * 2) <android.transition.TransitionManager: void go(android.transition.Scene)>:[19,25]
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkContentTransition();
    }

    @SuppressLint("NewApi")
    public void checkContentTransition()
    {
        //<android.app.Activity: android.transition.TransitionManager getContentTransitionManager()>:[21,25]
        TransitionManager mTransitionManager = this.getContentTransitionManager();

        //<android.transition.TransitionManager: void go(android.transition.Scene)>:[19,25]
        mTransitionManager.go(null);
    }

    @Override
    public android.transition.TransitionManager getContentTransitionManager() {
        return super.getContentTransitionManager();
    }
}
