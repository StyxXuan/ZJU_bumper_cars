package com.example.zju_bumper_cars.ViewLayer;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.zju_bumper_cars.config.glConfig;
import com.example.zju_bumper_cars.utils.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.example.zju_bumper_cars.ModelLayer.ModelGroup.draw;

public class MyRenderer implements GLSurfaceView.Renderer {
    public MySurfaceView mSurfaceView;
    public MyRenderer(MySurfaceView glSurfaceView) {
        this.mSurfaceView = glSurfaceView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // 设置为打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // 初始化变换矩阵
        MatrixState.setInitStack();
        //初始化定位光光源
        MatrixState.setLightLocation(40, 10, 20);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        //
        float ratio = (float) width / height;
        // 平行投影
		MatrixState.setProjectOrtho(-ratio, ratio, -1, 1,
				glConfig.PROJECTION_NEAR, glConfig.PROJECTION_FAR);
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1,
                glConfig.PROJECTION_NEAR, glConfig.PROJECTION_FAR);
        // camera
        MatrixState.setCamera(glConfig.EYE_X, glConfig.EYE_Y, glConfig.EYE_Z,
                glConfig.VIEW_CENTER_X, glConfig.VIEW_CENTER_Y, glConfig.VIEW_CENTER_Z,
                0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        //开启混合
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
        mSurfaceView.drawSelf();
    }
}
