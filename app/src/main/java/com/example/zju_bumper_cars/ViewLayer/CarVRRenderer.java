package com.example.zju_bumper_cars.ViewLayer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.zju_bumper_cars.ModelLayer.VrModules.SkyBox;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.R;
import com.example.zju_bumper_cars.gl.Model;
import com.example.zju_bumper_cars.gl.Texture;
import com.example.zju_bumper_cars.utils.Shader;
import com.example.zju_bumper_cars.utils.vec;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;

public class CarVRRenderer implements GvrView.StereoRenderer {

    private Context context;

    private Shader modelShader;
    private Model car;
    private Texture carAlbedo;

    private float[] inverseModel = new float[16];
    private float[] camera = new float[16];
    private float[] view = new float[16];
    private float[] mvp = new float[16];
    private float[] vp = new float[16];

    private float zNear = 0.01f;
    private float zFar = 20.0f;

    public CarVRRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {

    }

    @Override
    public void onDrawEye(Eye eye) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.multiplyMM(view, 0, eye.getEyeView(), 0, camera, 0);
        Matrix.multiplyMM(vp, 0, eye.getPerspective(zNear, zFar), 0, view, 0);
        Matrix.multiplyMM(mvp,0, vp, 0, car.modelMatrix, 0);

        modelShader.use();
        modelShader.setMatrix4("u_MVP", mvp);
        car.draw(modelShader);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int width, int height) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LESS);

        Matrix.setLookAtM(camera, 0, 0f, 0f, -5.0f, 0f, 0f, 0f, 0f, 1f, 0f);

        modelShader = Shader.create(context, R.raw.model_vertex_shader, R.raw.model_fragment_shader);
        try {

            carAlbedo = Texture.load(context,"camaro.jpg", Texture.ALBEDO);

            car = Model.load(context, "camaro.obj");
            car.setAttr(Model.VERTEX_ATTR, modelShader.getAttribute("a_Position"))
                    .setAttr(Model.UV_ATTR, modelShader.getAttribute("a_Uv"))
                    .setAttr(Model.NORMAL_ATTR, modelShader.getAttribute("a_Normal"))
                    .setOnDraw(new Model.OnDrawCall() {
                        @Override
                        public void onDraw(Shader shader) {
                            shader.setMatrix4("u_ModelMatrix", car.modelMatrix);
                            Matrix.invertM(inverseModel, 0, car.modelMatrix, 0);
                            shader.setMatrix4("u_InverseModelMatrix", inverseModel);
                            carAlbedo.active();
                            shader.setInt("u_Albedo", carAlbedo.getGLTexture());
                        }
                    })
                    .rotate(-90, 0,0,1)
                    .rotate(-90, 0, 1, 0);

        } catch (IOException e) {
            e.printStackTrace();
            car = null;
            carAlbedo = null;
        }
    }

    @Override
    public void onRendererShutdown() {

    }
}
