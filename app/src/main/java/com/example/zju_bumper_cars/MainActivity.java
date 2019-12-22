package com.example.zju_bumper_cars;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.zju_bumper_cars.ControlLayer.controlers.player_controler;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;

public class MainActivity extends Activity {
    public static float WIDTH;
    public static float HEIGHT;
    MySurfaceView mview;
    ImageButton BtnUp, BtnDown, BtnLeft, BtnRight;
    boolean DownPress = false;
    boolean UpPress = false;
    boolean LeftPress = false;
    boolean RightPress = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mview = new MySurfaceView(this);
        mview = (MySurfaceView) findViewById(R.id.glscen);
        initBtn();
        mview.requestFocus();//获取焦点
        mview.setFocusableInTouchMode(true);//设置为可触控

//        setContentView(mview);

    }
    @Override
    protected void onResume() {
        super.onResume();
        mview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mview.onPause();
    }

    public void initBtn(){
        BtnDown = findViewById(R.id.btn_down);
        BtnUp = findViewById(R.id.btn_up);
        BtnLeft = findViewById(R.id.btn_left);
        BtnRight = findViewById(R.id.btn_right);


        BtnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    BtnDown.setBackground(getResources().getDrawable(R.mipmap.down_w));
                    DownPress = true;
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            while(DownPress){
                                player_controler.goBack();
                            }
                        }
                    }.start();
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    DownPress = false;
                    BtnDown.setBackground(getResources().getDrawable(R.mipmap.down));
                }
                return false;
            }
        });

        BtnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    BtnUp.setBackground(getResources().getDrawable(R.mipmap.up_w));
                    UpPress = true;
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            while(UpPress){
                                player_controler.goStraght();
                            }
                        }
                    }.start();
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    UpPress = false;
                    BtnUp.setBackground(getResources().getDrawable(R.mipmap.up));
                }
                return false;
            }
        });

        BtnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    BtnLeft.setBackground(getResources().getDrawable(R.mipmap.left_w));
                    LeftPress = true;
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            while(LeftPress){
                                player_controler.ChangeDerectionLeft();
                            }
                        }
                    }.start();
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    LeftPress = false;
                    BtnLeft.setBackground(getResources().getDrawable(R.mipmap.left));
                }
                return false;
            }
        });

        BtnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    BtnRight.setBackground(getResources().getDrawable(R.mipmap.right_w));
                    RightPress = true;
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            while(RightPress){
                                player_controler.ChageDerectionRight();
                            }
                        }
                    }.start();
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    RightPress = false;
                    BtnRight.setBackground(getResources().getDrawable(R.mipmap.right));
                }
                return false;
            }
        });
    }

}