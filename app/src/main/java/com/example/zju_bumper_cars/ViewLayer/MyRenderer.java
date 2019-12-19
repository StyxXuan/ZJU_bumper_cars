package com.example.zju_bumper_cars.ViewLayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import com.example.zju_bumper_cars.ModelLayer.map.Constant;
import com.example.zju_bumper_cars.ModelLayer.models.Mountion;
import com.example.zju_bumper_cars.R;
import com.example.zju_bumper_cars.config.glConfig;
import com.example.zju_bumper_cars.utils.MatrixState;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MyRenderer implements GLSurfaceView.Renderer {
    public MySurfaceView mSurfaceView;
    public MyRenderer(MySurfaceView glSurfaceView) {
        this.mSurfaceView = glSurfaceView;
    }
    Mountion mountion;
    //山的纹理id
    int mountionId;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // 设置为打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // 初始化变换矩阵
        MatrixState.setInitStack();
        //初始化定位光光源
        MatrixState.setLightLocation(40, 10, 20);
        Constant.yArray=Constant.loadLandforms(mSurfaceView.getResources(), R.mipmap.land);
        mountion=new Mountion(mSurfaceView, Constant.yArray,
                Constant.yArray.length-1,Constant.yArray[0].length-1);
        mountionId = initTexture(R.mipmap.grass);
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
//        gl10.glEnable(GL10.GL_BLEND);
//        gl10.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
        MatrixState.pushMatrix();
        mountion.drawSelf(mountionId);
        MatrixState.popMatrix();
        mSurfaceView.drawSelf();
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
        InputStream is = mSurfaceView.getResources().openRawResource(drawableId);
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
}
