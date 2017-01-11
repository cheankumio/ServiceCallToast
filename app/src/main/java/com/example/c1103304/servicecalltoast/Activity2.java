package com.example.c1103304.servicecalltoast;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by c1103304 on 2017/1/11.
 */

public class Activity2 extends AppCompatActivity{
    Button btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2);
        MyService.curversion= MyService.version;

    }
    public void topage1(View view){

        finish();

    }
}
