package lu.uni.snt.apicompatibility_varargs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.security.keystore.KeyProtection;
import android.support.v7.app.AppCompatActivity;

/**
 * The constructor method of KeyProtection.Builder and method setBlockModes() are introduced at API level 23, while the minSdkVersion is set to 10
 * Therefore, this app will crash on devices running a platform with an API version smaller than 23.
 *
 * E/AndroidRuntime: FATAL EXCEPTION: main
 *                   java.lang.NoClassDefFoundError: android.security.keystore.KeyProtection$Builder
 *
 * This app contains two API compatibility issues:
 * 1) <android.security.keystore.KeyProtection.Builder: void <init>(int)>:[23,25]
 * 2) <android.security.keystore.KeyProtection.Builder: android.security.keystore.KeyProtection.Builder setBlockModes(java.lang.String...)>:[23,25]
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkStorageEncryption();
    }

    @SuppressLint("NewApi")
    public void checkStorageEncryption()
    {
        //<android.security.keystore.KeyProtection.Builder: void <init>(int)>:[23,25]
        KeyProtection.Builder builder = new KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);

        //<android.security.keystore.KeyProtection.Builder: android.security.keystore.KeyProtection.Builder setBlockModes(java.lang.String...)>:[23,25]
        builder.setBlockModes(KeyProperties.BLOCK_MODE_CBC, KeyProperties.BLOCK_MODE_CTR);
    }
}
