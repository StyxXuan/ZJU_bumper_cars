package com.example.zju_bumper_cars.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;

public class Texture {
    private int[] textureID = new int[1];

    private int type;

    public static final int ALBEDO = 0;
    public static final int NORMAL = 1;
    public static final int ROUGHNESS = 2;
    public static final int METALLIC = 3;

    private Texture(Context context, String assetsPath, int type) throws IOException{
        GLES20.glGenTextures(1, textureID, 0);
        this.type = type;
        active();
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        Bitmap textureBitmap = BitmapFactory.decodeStream(context.getAssets().open(assetsPath));
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmap, 0);
        textureBitmap.recycle();
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

    }

    public static Texture load(Context context, String assetsPath, int type) throws IOException{
        return new Texture(context, assetsPath, type);
    }

    public void active(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + type);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
    }

    public int getGLTexture(){
        return type;
    }

}
