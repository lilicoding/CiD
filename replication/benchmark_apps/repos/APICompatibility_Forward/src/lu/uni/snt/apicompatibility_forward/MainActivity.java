package lu.uni.snt.apicompatibility_forward;

import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		checkGetAssetInt();
	}
	
	public void checkGetAssetInt()
	{
		InputStream is = this.getResources().openRawResource(R.drawable.ic_launcher);
	    if (is instanceof AssetManager.AssetInputStream) {
	        int assetInt = ((AssetManager.AssetInputStream) is).getAssetInt();
	        
	        Log.d("AC_Forward", "" + assetInt);
	    }
	}
}
