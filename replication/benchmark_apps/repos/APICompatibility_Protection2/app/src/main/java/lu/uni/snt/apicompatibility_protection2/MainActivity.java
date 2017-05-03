package lu.uni.snt.apicompatibility_protection2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * setExact() is introduced at API level 19 and the minSdkVersion is set to 10.
 * Since the access of API setExact() has been protected by checking the current platform version,
 * this app will not crash on devices running a platform with an API version smaller than 19.
 *
 * If a compatibility checker has somehow reported this API as an API compatibility issue,
 * it unfortunately reveals a false alarm.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmManager alarmManager = null;

        if (Build.VERSION.SDK_INT >= 19)
        {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        checkAlarmSetExact(alarmManager);
    }

    @SuppressLint("NewApi")
    public void checkAlarmSetExact(AlarmManager alarmManager)
    {
        if (null != alarmManager)
        {
            //isEncrypted() is introduced at API level 19
            alarmManager.setExact(-1, -1, null);
    }
        else
        {
            Log.d("AC_Protection2", "AlarmManager.setExact(int,long,android.app.PendingIntent) is not available.");
        }

    }
}
