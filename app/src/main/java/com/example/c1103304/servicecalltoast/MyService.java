package com.example.c1103304.servicecalltoast;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by c1103304 on 2017/1/11.
 */

public class MyService extends Service {
    public static int version;
    public static final String versiondata="OldVersion";
    public static final String NowVersiondata="NowVersion";
    private Handler handler = new Handler();
    private Handler handler2 = new Handler();
    public static boolean nowState=false;
    public static int curversion=0;
    SharedPreferences oldversionnum;
    PendingIntent pendingIntent;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MYLOG","執行onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MYLOG","執行Service");
        handler.postDelayed(showTime, 1000);
        handler2.postDelayed(updata, 8000);
        oldversionnum = getSharedPreferences("DATA",0);
        version = oldversionnum.getInt(versiondata,0);
        curversion = oldversionnum.getInt(NowVersiondata,0);
        Log.d("MYLOG","Num: "+version);
        Intent page2 = new Intent(this,Activity2.class);
        pendingIntent = PendingIntent.getActivity(this,0,page2,0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        nowState = false;
        Log.d("MYLOG","結束Service");
    }

    private Runnable showTime = new Runnable() {
        public void run() {
            Bundle message = new Bundle();
            if(version>curversion) {
                //傳送version至主程式
                message.putInt("Key", version);
                Intent intent = new Intent("version");
                intent.putExtras(message);
                sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "更新資訊", Toast.LENGTH_LONG).show();
                notification();
                nowState = true;
            }
            Log.d("MYLOG","Num: "+version+"  cur: "+curversion);
            handler.postDelayed(this, 1000);
        }
    };

    private Runnable updata = new Runnable() {
        public void run() {
            version++;
            handler2.postDelayed(this, 8000);
        }
    };
    private void notification(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification ns = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("更新訊息")
                .setContentText("版本: "+version)
                .setContentIntent(pendingIntent)
                .build();
        ns.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(2,ns);
    }
}
