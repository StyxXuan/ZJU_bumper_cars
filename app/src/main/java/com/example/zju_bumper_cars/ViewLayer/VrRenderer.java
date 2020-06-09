package com.example.zju_bumper_cars.ViewLayer;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import com.example.zju_bumper_cars.ModelLayer.VrModules.BaseSkyBox;
import com.example.zju_bumper_cars.ModelLayer.VrModules.SkyBox;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public class VrRenderer extends BaseVrRenderer {


    private static final String TAG = "SkyBoxRender";

    public VrRenderer(Context context) {
        super(context);
    }

    public VrRenderer(Context context, GvrView gvrView) {
        super(context, gvrView);
    }

    @Override
    protected float getZ_NEAR() {
        return 0.001f;
    }

    @Override
    protected float getZ_FAR() {
        return 1000f;
    }

    @Override
    protected void setCameraLocation(float[] camera) {
        Matrix.setLookAtM(camera, 0, 0f, 0f, 0.01f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    protected void initSkyBoxMatrix(float[] model) {
        setIdentityM(model, 0);
        translateM(model, 0, 0f, 0f, 0f);
    }

    @Override
    protected BaseSkyBox getSkyBox() {
        mSkyBox = new SkyBox(mContext);
        return mSkyBox;
    }

    @Override
    protected void newFrame(HeadTransform headTransform) {

    }


    @Override
    public void finishFrame(Viewport viewport) {
    }


    private SkyBox mSkyBox;
    private final float[] model = new float[16];
    private final float[] modelView = new float[16];
    private float[] modelViewProjection = new float[16];

    @Override
    public void surfaceCreated(EGLConfig eglConfig) {
        updateTextureLocation(0f, 0f, 0f);
    }

    public void updateTextureLocation(float x, float y, float z) {
        setIdentityM(model, 0);
        translateM(model, 0, x, y, z);
    }

    @Override
    public void surfaceChanged(int i, int i1) {

    }

    @Override
    public void rendererShutdown() {
        Log.e(TAG, "--------------rendererShutdown: ");
        mSkyBox.destroy();
    }
}