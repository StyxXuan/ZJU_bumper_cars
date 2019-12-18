package com.example.zju_bumper_cars.ModelLayer;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.example.zju_bumper_cars.IOLayer.Obj.ObjLoaderUtil;
import com.example.zju_bumper_cars.ModelLayer.models.BaseModel;
import com.example.zju_bumper_cars.ModelLayer.models.glBasicObj;
import com.example.zju_bumper_cars.ModelLayer.models.glColorObj;
import com.example.zju_bumper_cars.ModelLayer.models.glTextureObj;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

public class ModelGroup {

//    private static Mountion map

    private static List<BaseModel> modelGroup;

    private static List<ObjLoaderUtil.ObjData> mObjList;

    private static List<glBasicObj> mObjSprites;

    public static void initModel(MySurfaceView surfaceView){
        modelGroup = new ArrayList<>();
        mObjList = new ArrayList<>();
        mObjSprites = new ArrayList<>();

        try {
            Log.d("info","Loading Objs");
            mObjList = ObjLoaderUtil.load("camaro.obj", surfaceView.getResources());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(mObjList == null) {
           Log.d("info","No objs");
           return;
        }

        for(int i=0; i<mObjList.size(); i++){
            ObjLoaderUtil.ObjData data = mObjList.get(i);
            //
            int diffuseColor = data.mtlData != null ? data.mtlData.Kd_Color : 0xffffffff;
            float alpha = data.mtlData != null ? data.mtlData.alpha : 1.0f;
            String texturePath = data.mtlData != null ? data.mtlData.Kd_Texture : "";

            // 构造对象
            if (data.aTexCoords != null && data.aTexCoords.length != 0 && TextUtils.isEmpty(texturePath) == false) {
                Log.d("info", "texture spirite");
                Bitmap bmp = BitmapUtil.getBitmapFromAsset(surfaceView.getContext(), texturePath);
                glTextureObj spirit = new glTextureObj(surfaceView, data.aVertices, data.aNormals, data.aTexCoords, alpha, bmp);
                mObjSprites.add(spirit);
            } else {
                Log.d("info", "color spirite");
                glColorObj spirit = new glColorObj(surfaceView, data.aVertices, data.aNormals, diffuseColor, alpha);
                mObjSprites.add(spirit);
            }
        }
    }

    public static void draw(){
        for(int i=0; i<mObjSprites.size(); i++){
            mObjSprites.get(i).drawSelf();
        }
    }

    public static void addModel(){}

    public static void deleteModel(){}
}
