package com.example.zju_bumper_cars.utils;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.text.TextUtils;
import android.util.Log;

import com.example.zju_bumper_cars.IOLayer.Obj.ObjLoaderUtil;
import com.example.zju_bumper_cars.ModelLayer.models.glBasicObj;
import com.example.zju_bumper_cars.ModelLayer.models.glColorObj;
import com.example.zju_bumper_cars.ModelLayer.models.glTextureObj;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<glBasicObj> DatatoOBJ(MySurfaceView surfaceView, List<ObjLoaderUtil.ObjData> mObjList){
        List<glBasicObj> mObjSprites = new ArrayList<>();
        if(mObjList == null) {
            Log.d("info","No objs");
            return null;
        }
        mObjSprites = new ArrayList<>();

        for(int i=0; i<mObjList.size(); i++){
            ObjLoaderUtil.ObjData data = mObjList.get(i);
            //
            int diffuseColor = data.mtlData != null ? data.mtlData.Kd_Color : 0xffffffff;
            float alpha = data.mtlData != null ? data.mtlData.alpha : 1.0f;
            String texturePath = data.mtlData != null ? data.mtlData.Kd_Texture : "";
            Log.d("info", texturePath);
            // 构造对象
            if (data.aTexCoords != null && data.aTexCoords.length != 0 && TextUtils.isEmpty(texturePath) == false) {
                Log.d("info", "texture spirite");
                Bitmap bmp = BitmapUtil.getBitmapFromAsset(surfaceView.getContext(), texturePath);
                glTextureObj spirit = new glTextureObj(surfaceView, data.aVertices, data.aNormals, data.aTexCoords, alpha, bmp);
                mObjSprites.add(spirit);
                Log.d("info", "add one spririte");
            } else {
                Log.d("info", "color spirite");
                glColorObj spirit = new glColorObj(surfaceView, data.aVertices, data.aNormals, diffuseColor, alpha);
                mObjSprites.add(spirit);
                Log.d("info", "add one spririte");
            }
        }
        return mObjSprites;
    }
}