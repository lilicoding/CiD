package lu.uni.snt.apicompatibility_generictype;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.TreeMap;

/**
 * replace() is introduced at API level 24, while the minSdkVersion is set to 10
 * Therefore, this app will crash on devices running a platform with an API version smaller than 24.
 *
 * E/AndroidRuntime: FATAL EXCEPTION: main
 *                   java.lang.NoSuchMethodError: java.util.TreeMap.replace
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkGenericType();
    }

    @TargetApi(24)
    public void checkGenericType()
    {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("GenericType", "NO");

        treeMap.replace("GenericType", "YES");
    }
}
