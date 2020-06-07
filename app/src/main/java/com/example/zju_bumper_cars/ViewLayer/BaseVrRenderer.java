package com.example.zju_bumper_cars.ViewLayer;

import android.content.Context;

import com.example.zju_bumper_cars.ModelLayer.VrModules.BaseSkyBox;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.ModelLayer.models.SkyBox;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.vec;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_LEQUAL;
import static android.opengl.GLES20.GL_LESS;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDepthFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public abstract class BaseVrRenderer  implements GvrView.StereoRenderer{
    protected Context mContext;
    private final float[] view = new float[16];
    private final float[] modelView = new float[16];
    private final float[] camera = new float[16];
    private final float[] model = new float[16];
    private float[] modelViewProjection = new float[16];
    private BaseSkyBox mSkyBox;
    private SkyBox skyBox;
    private float[] perspective;
    private Cars cars;
    GvrView gvrView;

    public BaseVrRenderer(Context context) {
        this.mContext = context;
    }

    public BaseVrRenderer(Context context, GvrView gvrView) {
        this.mContext = context;
        this.gvrView = gvrView;
    }


    @Override
    public void onNewFrame(HeadTransform headTransform) {
        setCameraLocation(camera);

        newFrame(headTransform);
    }

    @Override
    public void onDrawEye(Eye eye) {

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);//开启深度测试后，这句话要加，不然会绘制失败
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        multiplyMM(view, 0, eye.getEyeView(), 0, camera, 0);
        perspective = eye.getPerspective(getZ_NEAR(), getZ_FAR());

        if (mSkyBox != null) {
            multiplyMM(modelView, 0, view, 0, model, 0);
            multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);
            mSkyBox.draw(modelViewProjection);
        }

        //MatrixState.pushMatrix();
        MatrixState.setmProjMatrix(perspective);
        MatrixState.setmVMatrix(view);
        cars.vrDraw();


//        drawEye(eye, perspective, view);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        finishFrame(viewport);
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        surfaceChanged(i, i1);
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        surfaceCreated(eglConfig);
        initSkyBoxMatrix(model);
        mSkyBox = getSkyBox();
        cars = new Cars(gvrView, new vec(0, 0, 0), new vec(270, 0, 0));
        skyBox = new SkyBox(gvrView);
    }

    @Override
    public void onRendererShutdown() {
        rendererShutdown();
    }

    protected abstract float getZ_NEAR();

    protected abstract float getZ_FAR();

    protected abstract void setCameraLocation(float[] camera);

    protected abstract void initSkyBoxMatrix(float[] model);

    protected void updateSkyBoxMatrix(float[] m, int offset) {
        if (m == null || offset < 0 || offset + 16 > m.length) {
            throw new RuntimeException("BaseSkyBoxRender的updateModelLocation的参数不对！");
        }
        System.arraycopy(model, 0, m, offset, offset + 16);
    }

    protected void updateSkyBoxLocation(float x, float y, float z) {
        setIdentityM(model, 0);
        translateM(model, 0, x, y, z);
    }

    protected void updateCameraMatrix(float[] m, int offset) {
        if (m == null || offset < 0 || offset + 16 > m.length) {
            throw new RuntimeException("BaseSkyBoxRender的updateCameraMatrix的参数不对！");
        }
        System.arraycopy(camera, 0, m, offset, offset + 16);
    }

    protected float[] getCameraMatrix() {
        float[] result = new float[camera.length];
        System.arraycopy(result, 0, camera, 0, camera.length);
        return result;
    }

    protected float[] getViewMatrix() {
        return view;
    }

    protected float[] getPerspectiveMatrix() {
        return perspective;
    }

    protected abstract BaseSkyBox getSkyBox();

    protected abstract void newFrame(HeadTransform headTransform);

    public void drawEye(Eye eye, float[] perspectiveMatrix, float[] viewMatrix){
        multiplyMM(modelView, 0, viewMatrix, 0, model, 0);
        multiplyMM(modelViewProjection, 0, perspectiveMatrix, 0, modelView, 0);
        cars.draw();
        multiplyMM(modelView, 0, viewMatrix, 0, model, 0);
        multiplyMM(modelViewProjection, 0, perspectiveMatrix, 0, modelView, 0);
        skyBox.draw();
    }

    public abstract void finishFrame(Viewport viewport);

    public abstract void surfaceCreated(EGLConfig eglConfig);

    public abstract void surfaceChanged(int i, int i1);

    public abstract void rendererShutdown();
}
