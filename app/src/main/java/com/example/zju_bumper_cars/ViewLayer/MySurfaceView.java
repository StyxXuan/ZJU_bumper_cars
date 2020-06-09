package com.example.zju_bumper_cars.ViewLayer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import com.example.zju_bumper_cars.ControlLayer.controlers.player_controler;
import com.example.zju_bumper_cars.MainActivity;
import com.example.zju_bumper_cars.ModelLayer.ModelGroup;
import com.example.zju_bumper_cars.ModelLayer.models.Particle;
import com.example.zju_bumper_cars.config.Constant;
import com.example.zju_bumper_cars.R;
import com.example.zju_bumper_cars.config.glConfig;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.vec;

import java.io.IOException;
import java.io.InputStream;

import static com.example.zju_bumper_cars.ModelLayer.ModelGroup.ParticleSystemReady;
import static com.example.zju_bumper_cars.ModelLayer.ModelGroup.Player;
import static com.example.zju_bumper_cars.ModelLayer.ModelGroup.initDown;
import static com.example.zju_bumper_cars.ModelLayer.ModelGroup.initModel;

public class MySurfaceView extends GLSurfaceView {


    public MySurfaceView(Context context) {
        super(context);
        initES2();
        initRender();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 初始化render
        initES2();
        initRender();

    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        // 初始化render
        initES2();
        initRender();

    }
    // 宽
    private float mSceneWidth = 720;
    // 高
    private float mSceneHeight = 1280;

    public void initRender(){
        MyRenderer render = new MyRenderer(this);
        this.setRenderer(render);
        // 渲染模式(被动渲染)
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public int initTexture(int drawableId)
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId=textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //ST方向纹理拉伸方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

        //通过输入流加载图片
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try
        {
            bitmapTmp = BitmapFactory.decodeStream(is);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        //实际加载纹理
        GLUtils.texImage2D
                (
                        GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                        0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
                        bitmapTmp, 			  //纹理图像
                        0					  //纹理边框尺寸
                );
        //自动生成Mipmap纹理
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //释放纹理图
        bitmapTmp.recycle();
        //返回纹理ID
        return textureId;
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //
        this.setSceneWidthAndHeight(this.getMeasuredWidth(),
                this.getMeasuredHeight());
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//角度缩放比例
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if(mPreviousY - y >= 10){
                    glConfig.distance++;
                }else if(mPreviousY - y <= -10){
                    glConfig.distance--;
                }

                if(mPreviousX - x >= 10){
                    glConfig.angle++;
                }else if(mPreviousX - x <= -10){
                    glConfig.angle--;
                }

            this.requestRender();//重绘画面
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isInintFinsh = false;
    }


    public void setSceneWidthAndHeight(float mSceneWidth, float mSceneHeight) {
        this.mSceneWidth = mSceneWidth;
        this.mSceneHeight = mSceneHeight;
    }

    public float getSceneWidth() {
        return mSceneWidth;
    }

    public float getSceneHeight() {
        return mSceneHeight;
    }

    private void initES2() {
        // 使用OpenGL ES 2.0
        setEGLContextClientVersion(2);
    }
    boolean isInintFinsh = false;

    public void drawSelf() {
        if (isInintFinsh == false) {
            initModel(this);
            isInintFinsh = true;
        }
        MatrixState.pushMatrix();

        if(Player != null){
            vec Pos = Player.getPos();
            vec direction = Player.getDirection();
            vec normal = Player.getNormal();
//            MatrixState.rotate(glConfig.angle, 1 ,0, 0);
//            MatrixState.rotate(-(float)direction.y, 0, 1, 0);
//            MatrixState.rotate(-(float)direction.z, 0, 0, 1);
//            MatrixState.rotate(-(float)direction.x, 1, 0, 0);
//            MatrixState.translate(-(float)Pos.x, (float)pos.y, (float)pos.z);
//            MatrixState.translate((float) (-Pos.x), glConfig.distance, (float) (-Pos.z));
            MatrixState.translate(0, glConfig.distance, -30);
            MatrixState.rotate(glConfig.angle, 1, 0, 0);
            MatrixState.rotate(-(float) direction.y, 0, 1, 0);
            MatrixState.translate((float) (-Pos.x), -(float)Pos.y, (float) (-Pos.z));

//            MatrixState.rotate((float) direction.y, 0, 1, 0);
//            MatrixState.rotate((float)direction.x, 1, 0, 0);
        }else{
            MatrixState.translate(0, 0, glConfig.distance);
        }


        ModelGroup.draw(this);
        MatrixState.popMatrix();

        if(!ParticleSystemReady){
//            if(glConfig.distance > -20){
//                glConfig.distance -= 1;
//            }else{
//                ParticleSystemReady = true;
//            }
            initDown = true;
        }
    }
}
