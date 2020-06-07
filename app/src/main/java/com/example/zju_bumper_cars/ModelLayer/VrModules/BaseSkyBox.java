package com.example.zju_bumper_cars.ModelLayer.VrModules;

import android.content.Context;
import android.util.Log;


import com.example.zju_bumper_cars.R;
import com.example.zju_bumper_cars.utils.CommonUtil;
import com.example.zju_bumper_cars.utils.Shader;
import com.example.zju_bumper_cars.utils.ShaderProgramUtil;
import com.example.zju_bumper_cars.utils.TextureUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * 类名
 * 创建时间 2016/12/12
 * 实现的主要功能
 *
 * @author zjc
 */

public abstract class BaseSkyBox extends Shape {

    private final float[] vertexArray = new float[]{
            -1, 1, 1,//0
            1, 1, 1,//1
            -1, -1, 1,//2
            1, -1, 1,//3
            -1, 1, -1,//4
            1, 1, -1,//5
            -1, -1, -1,//6
            1, -1, -1 //7
    };
    private final byte[] indexArray = {
            //front
            1, 3, 0,
            0, 3, 2,

            //back
            4, 6, 5,
            5, 6, 7,

            //left
            0, 2, 4,
            4, 2, 6,

            //right
            5, 7, 1,
            1, 7, 3,

            //top
            5, 1, 4,
            4, 1, 0,

            //bottom
            6, 2, 7,
            7, 2, 3
    };

    private Shader shader;
    private ByteBuffer indexBuffer;
    private FloatBuffer vertexBuffer;
    private int mPositionHandler;
    private int mSkyboxTexture;
    private static final int POSITON_COMPONENT_COUNT = 3;

    public BaseSkyBox(Context context) {
        super(context);
        initData();
    }

    @Override
    protected void createProgram() {
        shader = Shader.create(mContext, R.raw.skybox_vertex_shader, R.raw.skybox_fragment_shader);
        mPositionHandler = shader.getAttribute("a_Position");
    }

    protected void initAttribute(){


    }

    @Override
    protected void initData() {
        mSkyboxTexture = getTextureId();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, mSkyboxTexture);

        if (mSkyboxTexture == 0) {
            Log.e("BaseSkyBox", "纹理未成功加载");
            return;
        }
        vertexBuffer = CommonUtil.newFloatBuffer(vertexArray);
        vertexBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indexArray.length)
                .order(ByteOrder.nativeOrder())
                .put(indexArray);
        indexBuffer.position(0);



        createProgram();
    }

    @Override
    public void draw(float[] mVPMatrix) {
        if (mSkyboxTexture == 0) {
            Log.e("BaseSkyBox", "纹理未成功加载");
            return;
        }

        shader.use()
                .setMatrix4("u_Matrix", mVPMatrix)
                .setInt("u_TextureUnit",0);
        glVertexAttribPointer(mPositionHandler, POSITON_COMPONENT_COUNT,
                GL_FLOAT, false, 0, vertexBuffer);
        //indexArray描绘的指引
        glEnableVertexAttribArray(mPositionHandler);
        glDrawElements(GL_TRIANGLES, indexArray.length, GL_UNSIGNED_BYTE, indexBuffer);
        glDisableVertexAttribArray(mPositionHandler);
    }

    /**
     * 加载六个面的纹理
     *
     * @return 纹理的处理ID
     */
    protected abstract int getTextureId();

    @Override
    public void destroy() {
        super.destroy();
        TextureUtil.delete(mSkyboxTexture);
    }
}
