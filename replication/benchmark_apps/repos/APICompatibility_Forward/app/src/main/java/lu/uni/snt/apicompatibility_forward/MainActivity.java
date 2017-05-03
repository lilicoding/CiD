package lu.uni.snt.apicompatibility_forward;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;

/**
 * getAssetInt() has been removed from the public Android SDK at API level 19 (and its behavior is changed at the framework side).
 * If this app is somehow launched on devices running a platform version higher than 19,
 * it, instead of return the asset int, will directly through an exception and thus cause this app to crash.
 *
 * E/AndroidRuntime: FATAL EXCEPTION: main
 *                   java.lang.RuntimeException: java.lang.UnsupportedOperationException
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		checkGetAssetInt();
	}
	
	public void checkGetAssetInt()
	{
		InputStream is = this.getResources().openRawResource(+ R.drawable.ic_launcher);
	    if (is instanceof AssetManager.AssetInputStream) {

            //<android.content.res.AssetManager.AssetInputStream: int getAssetInt()>:[1,19]
	        int assetInt = ((AssetManager.AssetInputStream) is).getAssetInt();
	        
	        Log.d("AC_Forward", "" + assetInt);
	    }
	}
}
