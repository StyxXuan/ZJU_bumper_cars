package com.example.zju_bumper_cars.ViewLayer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.SurfaceView;

import com.example.zju_bumper_cars.R;
import com.example.zju_bumper_cars.gl.Model;
import com.example.zju_bumper_cars.gl.Texture;
import com.example.zju_bumper_cars.utils.Shader;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class WelcomeViewRenderer implements GLSurfaceView.Renderer {

    private Context context;

    private Shader modelShader;
    private Shader skyboxShader;


    private Texture carAlbedo;
    private Texture skyboxCube;
    private Model car;
    private Model skybox;


    private float[] view = new float[16];
    private float[] projection = new float[16];
    private float[] mvp = new float[16];
    private float[] inverseModel = new float[16];
    private float[] vp = new float[16];


    public WelcomeViewRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LESS);


        Matrix.setLookAtM(view, 0, 0, 2, -4.0f, 0f, 0f, 0f, 0, 1, 0f);

        modelShader = Shader.create(context, R.raw.model_vertex_shader, R.raw.model_fragment_shader);
        skyboxShader = Shader.create(context, R.raw.skybox_vertex_shader, R.raw.skybox_fragment_shader);

        modelShader.setFloat3("u_LightLocation", 0, 2, -4)
                .setFloat3("u_LightColor", 0.6f, 0.6f, 0.6f)
                .setFloat("u_Shiness",32.0f);

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
                            Matrix.transposeM(inverseModel, 0, inverseModel, 0);
                            shader.setMatrix4("u_InverseModelMatrix", inverseModel);
                            carAlbedo.active();
                            shader.setInt("u_Albedo", carAlbedo.getGLTexture());
                        }
                    })
                    .rotate(-90, 0,0,1)
                    .rotate(-90, 0, 1, 0);

            skyboxCube = Texture.load(context, new String[]{"Left.jpg",
                    "Right.jpg",
                    "Bottom.jpg",
                    "Top.jpg",
                    "Front.jpg",
                    "Back.jpg"
            }, Texture.SKYBOX);

            skybox = Model.load(context, "sky2.obj");
            skybox.setAttr(Model.VERTEX_ATTR, skyboxShader.getAttribute("a_Position"))
                    .setOnDraw(new Model.OnDrawCall() {
                        @Override
                        public void onDraw(Shader shader) {
                            skyboxCube.active();
                            shader.setInt("u_TextureUnit", skyboxCube.getGLTexture());
                        }
                    })
                    .scale(5,5,5);

        } catch (IOException e) {
            e.printStackTrace();
            car = null;
            carAlbedo = null;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float aspect = (float) width / (float) height;
        Matrix.perspectiveM(projection, 0, 45, aspect, 0.01f, 10.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.multiplyMM(vp, 0,projection, 0, view, 0);
        Matrix.multiplyMM(mvp,0, vp, 0, car.modelMatrix, 0);
        modelShader.use();
        modelShader.setMatrix4("u_MVP", mvp)
                .setFloat3("u_CameraPosition", 0, 2, -4);

        car.rotate(0.1f, 0,0,1);
        car.draw(modelShader);

        skyboxShader.use();
        skyboxShader.setMatrix4("u_Matrix", vp);
        skybox.draw(skyboxShader);
    }
}
