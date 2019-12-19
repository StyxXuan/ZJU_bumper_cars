package com.example.zju_bumper_cars.ViewLayer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.example.zju_bumper_cars.ModelLayer.ModelGroup;
import com.example.zju_bumper_cars.utils.MatrixState;

import static com.example.zju_bumper_cars.ModelLayer.ModelGroup.initModel;

public class MySurfaceView extends GLSurfaceView {

    public MySurfaceView(Context context) {
        super(context);

        initES2();
        initModel(this);
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
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算触控笔Y位移
                float dx = x - mPreviousX;//计算触控笔X位移
                //
//                float yAngle = mSpriteGroup.getSpriteAngleY();
//                yAngle += dx * TOUCH_SCALE_FACTOR;
//                mSpriteGroup.setSpriteAngleY(yAngle);

                this.requestRender();//重绘画面
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return super.onTouchEvent(event);
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
        MatrixState.translate(0, 0, -10);
        //
        ModelGroup.draw();
        MatrixState.popMatrix();

    }

}
