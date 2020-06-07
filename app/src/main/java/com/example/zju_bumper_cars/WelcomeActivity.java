package com.example.zju_bumper_cars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    public static float WIDTH;
    public static float HEIGHT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //        设置为全屏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //获得系统的宽度以及高度
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels>dm.heightPixels) {
            WIDTH=dm.widthPixels;
            HEIGHT=dm.heightPixels;
        } else {
            WIDTH=dm.heightPixels;
            HEIGHT=dm.widthPixels;
        }
        //设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        startMainActivity();
    }

    public void startMainActivity(View view){
        Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(mainIntent);

//        TimerTask delayTask = new TimerTask() {
//            @Override
//            public void run() {
//
//                WelcomeActivity.this.finish();
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(delayTask,2000);//延时两秒执行 run 里面的操作
    }

    public void startVRActivity(View v) {
        startActivity(new Intent(this, VrActivity.class));
    }
}
