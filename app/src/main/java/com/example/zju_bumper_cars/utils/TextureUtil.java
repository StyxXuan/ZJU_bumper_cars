package com.example.zju_bumper_cars.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_NEAREST_MIPMAP_NEAREST;
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
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameterf;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

public class TextureUtil {

    /**
     * @param context
     * @param drawableId
     * @return
     */
    public static int getTextureIdByDrawableId(Context context, int drawableId)// textureId
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        //通过输入流加载图片===============begin===================
        InputStream is = context.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try {
            bitmapTmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end=====================
        GLUtils.texImage2D
                (
                        GLES20.GL_TEXTURE_2D, //纹理类型
                        0,
                        GLUtils.getInternalFormat(bitmapTmp),
                        bitmapTmp, //纹理图像
                        GLUtils.getType(bitmapTmp),
                        0 //纹理边框尺寸
                );
        bitmapTmp.recycle();          //纹理加载成功后释放图片
        return textureId;
    }


    /**
     * @param bmp
     * @return
     */
    public static int getTextureIdByBitmap(Bitmap bmp)// textureId
    {
        // 生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures(1, // 产生的纹理id的数量
                textures, // 纹理id的数组
                0 // 偏移量
        );
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        // 实际加载纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, // 纹理类型，在OpenGL
                // ES中必须为GL10.GL_TEXTURE_2D
                0, // 纹理的层次，0表示基本图像层，可以理解为直接贴图
                bmp, // 纹理图像
                0 // 纹理边框尺寸
        );
        bmp.recycle(); // 纹理加载成功后释放图片

        return textureId;
    }

    public static int loadTexture(Context context, int resourceId) {
        int[] textureObjectIds = loadTextureIds();
        final Bitmap bitmap = createNoScaleBitmap(context, resourceId);
        validateBitmap(textureObjectIds, bitmap);
        //绑定纹理，才可进行对纹理操作
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        // http://blog.csdn.net/pizi0475/article/details/49740879
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        configureTexture2D(GL_LINEAR_MIPMAP_LINEAR,
                GL_LINEAR, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE);
        //加载位图数据到纹理
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        //生成MIP贴图
        glGenerateMipmap(GL_TEXTURE_2D);
        //解除纹理绑定
        glBindTexture(GL_TEXTURE_2D, 0);
        return textureObjectIds[0];
    }

    public static int loadTexture(Context context, int resourceId,
                                  int min_filter, int mag_filter, int wrap_s, int wrap_t) {
        int[] textureObjectIds = loadTextureIds();
        final Bitmap bitmap = createNoScaleBitmap(context, resourceId);
        validateBitmap(textureObjectIds, bitmap);
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        configureTexture2D(min_filter,
                mag_filter, wrap_s, wrap_t);
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        if (isUsingMipmap(min_filter, mag_filter))
            glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
        return textureObjectIds[0];
    }

    public static int loadTextureId() {
        return loadTextureIds()[0];
    }

    public static int[] loadTextureIds() {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            throw new RuntimeException("创建纹理对象失败");
        }

        return textureObjectIds;
    }

    /**
     * opengl纹理过滤模式
     *
     * @param min_filter
     * @param mag_filter
     * @param wrap_s
     * @param wrap_t
     */
    private static void configureTexture2D(int min_filter, int mag_filter, int wrap_s, int wrap_t) {

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min_filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag_filter);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap_s);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap_t);
    }

    /**
     * 加载立方体纹理
     *
     * @param context
     * @param cubeResources 图片顺序对应为立方体的左右、下上、前后面
     * @return
     */
    public static int loadCubeMap(Context context, int[] cubeResources) {
        final int[] textureObjectIds = loadTextureIds();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap[] cubeBitmaps = new Bitmap[6];

        for (int i = 0; i < 6; i++) {
            cubeBitmaps[i] = BitmapFactory.decodeResource(context.getResources(),
                    cubeResources[i], options);

            if (cubeBitmaps[i] == null) {
                glDeleteTextures(1, textureObjectIds, 0);
                throw new RuntimeException("第" + i + "个资源不能被加载");
            }
        }

        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0]);

        //过滤纹理模式使用双线性过滤
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //立方体左右、下上、前后传递面
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0);

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0);

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0);

        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

        for (Bitmap bitmap : cubeBitmaps) {
            bitmap.recycle();
        }

        return textureObjectIds[0];
    }


    public static Bitmap createNoScaleBitmap(Context context, int resourceId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;//图像原始数据

        return BitmapFactory.decodeResource(context.getResources(),
                resourceId, options);
    }

    public static void validateBitmap(int[] textureId, Bitmap bitmap) {
        if (bitmap == null) {
            glDeleteTextures(1, textureId, 0);
            throw new RuntimeException("bitmap资源加载失败");
        }
    }

    public static boolean isUsingMipmap(int min_filter, int mag_filter) {
        return GL_NEAREST_MIPMAP_NEAREST <= min_filter && min_filter <= GL_LINEAR_MIPMAP_LINEAR
                || GL_NEAREST_MIPMAP_NEAREST <= mag_filter && mag_filter <= GL_LINEAR_MIPMAP_LINEAR;
    }

    public static void delete(int textureId) {
        glDeleteTextures(1, new int[]{textureId}, 0);
    }

}
