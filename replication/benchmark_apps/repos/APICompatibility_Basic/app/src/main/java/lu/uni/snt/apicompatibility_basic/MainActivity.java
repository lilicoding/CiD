package lu.uni.snt.apicompatibility_basic;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * setExact() is introduced at API level 19, while the minSdkVersion is set to 10
 * Therefore, this app will crash on devices running a platform with an API version smaller than 19.
 *
 * E/AndroidRuntime: FATAL EXCEPTION: main
 *                   java.lang.NoSuchMethodError: android.app.AlarmManager.setExact
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAlarmSetExact();
    }

    @SuppressLint("NewApi")
    public void checkAlarmSetExact()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //isEncrypted() is introduced at API level 19
        alarmManager.setExact(-1, -1, null);
    }
}
