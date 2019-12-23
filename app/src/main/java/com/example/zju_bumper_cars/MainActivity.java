package com.example.zju_bumper_cars;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.zju_bumper_cars.ControlLayer.controlers.player_controler;
import com.example.zju_bumper_cars.ModelLayer.ModelGroup;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.config.glConfig;
import com.plattysoft.leonids.ParticleSystem;

import java.text.SimpleDateFormat;

public class MainActivity extends Activity {
    public static float WIDTH;
    public static float HEIGHT;
    Handler handler;
    MySurfaceView mview;
    ImageButton BtnUp, BtnDown, BtnLeft, BtnRight;
    public static Button BtnCollision;
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
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d("collision", "particle sys start");
                ParticleSystem ps = new ParticleSystem(MainActivity.this, 100, R.drawable.star_pink, 800);
                ps.setScaleRange(0.7f, 1.3f);
                ps.setSpeedRange(0.1f, 0.25f);
                ps.setRotationSpeedRange(90, 180);
                ps.setFadeOut(200, new AccelerateInterpolator());
                ps.emit(glConfig.COLLISTION_X, glConfig.COLLISTION_Y, 200, 200);
            }
        };

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
        BtnCollision = findViewById(R.id.btn_collision);

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

//        BtnCollision.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ParticleSystem ps = new ParticleSystem(MainActivity.this, 100, R.drawable.star_pink, 800);
//                ps.setScaleRange(0.7f, 1.3f);
//                ps.setSpeedRange(0.1f, 0.25f);
//                ps.setRotationSpeedRange(90, 180);
//                ps.setFadeOut(200, new AccelerateInterpolator());
//                ps.emit(glConfig.COLLISTION_X, glConfig.COLLISTION_Y, glConfig.COLLISTION_Z, 200);
//            }
//        });

        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    if(ModelGroup.CollisionHapen){
                        for(Cars car:ModelGroup.ALLPlayer){
                            if (car.onCollision){
                                car.onCollision = false;
                                glConfig.COLLISTION_X = (int)car.pos.x + 100;
                                glConfig.COLLISTION_Y = -(int)car.pos.y + 100;
//                                glConfig.COLLISTION_X /= WIDTH;
//                                glConfig.COLLISTION_Y /= HEIGHT;
                                handler.sendEmptyMessage(0);
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        ModelGroup.CollisionHapen = false;
                    }
                    if(glConfig.LIGHT_POS_X <= -360 | glConfig.LIGHT_POS_X >= 360){
                        glConfig.LIGHT_DISPLACEMENT = -glConfig.LIGHT_DISPLACEMENT;
                    }
                    glConfig.LIGHT_POS_X += glConfig.LIGHT_DISPLACEMENT;
                }
            }
        }.start();
    }

}