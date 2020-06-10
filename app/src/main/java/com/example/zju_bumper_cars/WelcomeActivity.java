package com.example.zju_bumper_cars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.example.zju_bumper_cars.ViewLayer.WelcomeViewRenderer;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    public static float WIDTH;
    public static float HEIGHT;

    private GLSurfaceView surfaceView;

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
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(new WelcomeViewRenderer(this));

//        startMainActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(surfaceView != null){
            surfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(surfaceView != null){
            surfaceView.onResume();
        }
    }

    public void startMainActivity(View view){
        Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(mainIntent);
    }

    public void startVRActivity(View v) {
        startActivity(new Intent(this, VrActivity.class));
    }
}
