package com.example.c1103304.servicecalltoast;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
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
    private Handler handler = new Handler();
    int version=0;
    int oldversion=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(showTime, 2000);
        oldversion = version;
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable showTime = new Runnable() {
        public void run() {
            //Log.d("MYLOG","running...");
            version = appversion.version;
            Bundle message = new Bundle();
            if(version>oldversion) {
                //傳送version至主程式
                message.putInt("Key", version);
                Intent intent = new Intent("version");
                intent.putExtras(message);
                sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Updata..", Toast.LENGTH_LONG).show();
                //notification();
                oldversion = version;
            }
            handler.postDelayed(this, 2000);
        }
    };


//    private void showDialog() {
//        AlertDialog mDialog = new AlertDialog.Builder(this).setTitle("Message")
//                .setMessage("testMessage")
//                .setCancelable(true).create();
//        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        mDialog.show();
//    }
}
