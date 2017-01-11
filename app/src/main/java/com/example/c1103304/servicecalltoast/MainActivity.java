package com.example.c1103304.servicecalltoast;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Intent mMyService;
    int version=0;
    int oldversion=0;
    SharedPreferences oldversionnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //儲存版本號
        oldversionnum = getSharedPreferences("DATA",0);
        oldversion = oldversionnum.getInt(appversion.versiondata,0);
        Log.d("MYLOG","APP Version: "+oldversion);

        //建立server監聽
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //取得版本號
                Bundle message = intent.getExtras();
                version = message.getInt("Key");
                 //判斷有無更新版本
                if(version>oldversion) {

                    Log.d("MYLOG","version Updata. \n now version: "+version);
                    oldversion = version;
                }
            }
        };

        //註冊過濾器
        IntentFilter filter = new IntentFilter("version");
        registerReceiver(receiver,filter);

        mMyService = new Intent(MainActivity.this, com.example.c1103304.servicecalltoast.MyService.class);

    }

    public void topage2(View view){
        Intent page2 = new Intent(MainActivity.this,Activity2.class);

        //開啟服務及跳轉Activity
        startService(mMyService);
        startActivity(page2);
    }

    public void addversionNUM(View view){
        appversion.version++;
    }

    public void reset(View view){
        oldversion = 0;
        oldversionnum.edit().putInt(appversion.versiondata,0).commit();
        //private void notification(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification ns = new Notification.Builder(getApplicationContext())
        .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("test Title")
                .setContentText("我是通知內容")
                .build();
        notificationManager.notify(2,ns);
        //}
    }

    @Override
    protected void onDestroy(){
        Log.d("MYLOG","Activity is Destroy");
        stopService(mMyService);
        //儲存目前版本號
        oldversionnum.edit().putInt(appversion.versiondata,oldversion).commit();
        super.onDestroy();
    }
}
