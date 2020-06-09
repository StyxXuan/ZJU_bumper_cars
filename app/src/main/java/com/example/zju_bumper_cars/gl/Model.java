package com.example.zju_bumper_cars.gl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.zju_bumper_cars.utils.Shader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;

public class Model {

    public static final int VERTEX_ATTR = 0;
    public static final int UV_ATTR = 1;
    public static final int NORMAL_ATTR = 2;
    public static final int TANGENT_ATTR = 3;
    public static final int BITANGENT_ATTR = 4;

    public float[] modelMatrix = new float[16];
    private float[] rotateM = new float[16];

    private final FloatBuffer vertex;
    private final FloatBuffer uv;
    private final FloatBuffer normal;
    private FloatBuffer tangent;
    private FloatBuffer biTangent;

    private ShortBuffer vertexIndex;

    private int[] attrs = new int[5];

    public interface OnDrawCall{
        void onDraw(Shader shader);
    }

    private OnDrawCall onDrawCall = null;


    private Model(Context context, String assetsPath) throws IOException {

        Arrays.fill(attrs, -1);
        Matrix.setIdentityM(modelMatrix, 0);

        InputStream inputStream = context.getAssets().open(assetsPath);
        Obj obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
        inputStream.close();

        IntBuffer intIndex = ObjData.getFaceVertexIndices(obj, 3);

        vertex = ObjData.getVertices(obj);
        uv = ObjData.getTexCoords(obj, 2);
        normal = ObjData.getNormals(obj);

        vertexIndex = ByteBuffer.allocateDirect(2 * intIndex.limit())
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();

        while (intIndex.hasRemaining()){
            vertexIndex.put((short) intIndex.get());
        }

        vertexIndex.rewind();

    }

    public static Model load(Context context, String assetsPath) throws IOException{
        return new Model(context, assetsPath);
    }

    public Model setAttr(int attr, int id){
        attrs[attr] = id;
        return this;
    }

    public Model setOnDraw(OnDrawCall function){
        onDrawCall = function;
        return this;
    }

    public Model translate(float dx, float dy, float dz){
        Matrix.translateM(modelMatrix, 0, dx, dy, dz);
        return this;
    }

    public Model scale(float sx, float sy, float sz){
        Matrix.scaleM(modelMatrix, 0,sx, sy, sz);
        return this;
    }

    public Model rotate(float angle, float x, float y, float z){
        Matrix.rotateM(modelMatrix, 0, angle, x, y, z);
        return this;
    }

    public void draw(Shader shader){

        if(onDrawCall != null){
            onDrawCall.onDraw(shader);
        }

        GLES20.glEnableVertexAttribArray(attrs[VERTEX_ATTR]);
        GLES20.glVertexAttribPointer(attrs[VERTEX_ATTR], 3, GLES20.GL_FLOAT, false, 0, vertex);

        if(attrs[UV_ATTR] != -1) {
            GLES20.glEnableVertexAttribArray(attrs[UV_ATTR]);
            GLES20.glVertexAttribPointer(attrs[UV_ATTR], 2, GLES20.GL_FLOAT, false, 0, uv);
        }

        if(attrs[NORMAL_ATTR] != -1){
            GLES20.glEnableVertexAttribArray(attrs[NORMAL_ATTR]);
            GLES20.glVertexAttribPointer(attrs[NORMAL_ATTR], 3, GLES20.GL_FLOAT, false, 0, normal);
        }

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, vertexIndex.limit(), GLES20.GL_UNSIGNED_SHORT, vertexIndex);

    }





}
