package com.example.c1103304.servicecalltoast;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Intent mMyService;
    int version=0;
    int oldversion=0;
    TextView versionbox;
    PendingIntent pendingIntent;
    SharedPreferences oldversionnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        versionbox = (TextView)findViewById(R.id.textView);

        //儲存版本號
        oldversionnum = getSharedPreferences("DATA",0);
        oldversion = oldversionnum.getInt(MyService.NowVersiondata,0);
        Log.d("MYLOG","APP Version: "+oldversion);

        //建立server監聽
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //取得版本號
                Bundle message = intent.getExtras();
                version = message.getInt("Key");
                Log.d("MYLOG","running...");
                 //判斷有無更新版本
                if(version>oldversion) {
                    Log.d("MYLOG","version>oldversion");
                    oldversion = version;
                    versionbox.setText("Version: "+version);
                }
            }
        };

        //註冊過濾器
        IntentFilter filter = new IntentFilter("version");
        registerReceiver(receiver,filter);
        if(!MyService.nowState) {
            //如果Service尚未啟動，則啟動服務
            mMyService = new Intent(MainActivity.this, com.example.c1103304.servicecalltoast.MyService.class);
            startService(mMyService);
            Log.d("MYLOG","啟動Service");
        }else{Log.d("MYLOG","Service已經啟動過了");}
        Intent page2 = new Intent(MainActivity.this,Activity2.class);
        pendingIntent = PendingIntent.getActivity(this,0,page2,0);
    }


    public void topage2(View view){
        Toast.makeText(getApplicationContext(), "我沒有功能", Toast.LENGTH_LONG).show();
    }
//    getApplicationContext()

    public void addversionNUM(View view){
        MyService.version++;
    }

    public void reset(View view){
        oldversion = 0;
        MyService.version = 0;
        oldversionnum.edit().putInt(MyService.versiondata,0)
                .putInt(MyService.NowVersiondata,0)
                .commit();
    }

    @Override
    protected void onDestroy(){
        Log.d("MYLOG","Activity is Destroy");
        //stopService(mMyService);
        //儲存目前版本號
        oldversionnum.edit().putInt(MyService.versiondata,version)
                .putInt(MyService.NowVersiondata,oldversion)
                .commit();
        super.onDestroy();
    }
}
