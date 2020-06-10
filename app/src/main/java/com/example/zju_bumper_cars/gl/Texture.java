package com.example.zju_bumper_cars.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.zju_bumper_cars.BuildConfig;

import java.io.IOException;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

public class Texture {
    private int[] textureID = new int[1];

    private int type;

    public static final int ALBEDO = 0;
    public static final int NORMAL = 1;
    public static final int ROUGHNESS = 2;
    public static final int METALLIC = 3;
    public static final int SKYBOX = 4;

    private Texture(Context context, String assetsPath, int type) throws IOException{
        this.type = type;
        Bitmap textureBitmap = BitmapFactory.decodeStream(context.getAssets().open(assetsPath));
        generate2DTexture(textureBitmap);
        textureBitmap.recycle();
    }

    private Texture(Context context, int resId, int type) throws IOException{
        this.type = type;
        Bitmap textureBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        generate2DTexture(textureBitmap);
        textureBitmap.recycle();
    }


    private Texture(Context context, int[] resIds, int type) throws IOException {
        if (BuildConfig.DEBUG && !(type == SKYBOX)) {
            throw new AssertionError("Assertion failed");
        }

        Bitmap[] bitmaps = new Bitmap[6];
        for(int i = 0;i < 6;i++){
            bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), resIds[i]);
        }
        generate3DTexture(bitmaps);

        for(Bitmap bitmap: bitmaps){
            if(bitmap != null){
                bitmap.recycle();
            }
        }
    }

    private Texture(Context context, String[] assets, int type) throws IOException {
        if (BuildConfig.DEBUG && !(type == SKYBOX)) {
            throw new AssertionError("Assertion failed");
        }

        Bitmap[] bitmaps = new Bitmap[6];
        for(int i = 0;i < 6;i++){
            bitmaps[i] = BitmapFactory.decodeStream(context.getAssets().open(assets[i]));
        }
        generate3DTexture(bitmaps);

        for(Bitmap bitmap: bitmaps){
            if(bitmap != null){
                bitmap.recycle();
            }
        }
    }

    private void generate2DTexture(Bitmap bitmap){
        GLES20.glGenTextures(1, textureID, 0);
        active();
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GL_TEXTURE_2D, 0);
    }


    private void generate3DTexture(Bitmap[] bitmaps){
        if(bitmaps.length < 6){
            throw new RuntimeException("Cube map not enough");
        }

        for(int i = 0;i < 6;i++){
            if(bitmaps[i] == null){
                throw new RuntimeException(i + " Texture not load");
            }
        }

        GLES20.glGenTextures(1, textureID, 0);
        active();

        //过滤纹理模式使用双线性过滤
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //立方体左右、下上、前后传递面
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, bitmaps[0], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, bitmaps[1], 0);

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, bitmaps[2], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, bitmaps[3], 0);

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, bitmaps[4], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, bitmaps[5], 0);

        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

    }

    public static Texture load(Context context, String assetsPath, int type) throws IOException{
        return new Texture(context, assetsPath, type);
    }

    public static Texture load(Context context, int resId, int type) throws IOException{
        return new Texture(context, resId, type);
    }

    public static Texture load(Context context, int[] resIds, int type) throws IOException{
        return new Texture(context, resIds, type);
    }

    public static Texture load(Context context, String[] assets, int type) throws IOException{
        return new Texture(context, assets, type);
    }

    public void active(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + type);
        if(type == SKYBOX){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureID[0]);
        }else {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
        }
    }

    public int getGLTexture(){
        return type;
    }

}
