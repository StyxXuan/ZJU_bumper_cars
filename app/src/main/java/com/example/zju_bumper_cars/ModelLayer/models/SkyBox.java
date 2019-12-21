package com.example.zju_bumper_cars.ModelLayer.models;

import android.util.Log;

import com.example.zju_bumper_cars.IOLayer.Obj.ObjLoaderUtil;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.Util;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

public class SkyBox extends BaseModel{

    private static String ObjPath = "try.obj";
    private List<glBasicObj> objs;

    public SkyBox(MySurfaceView mySurfaceView){
        List<ObjLoaderUtil.ObjData> mObjList = new ArrayList<>();
        try {
            mObjList.addAll(ObjLoaderUtil.load(ObjPath, mySurfaceView.getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        objs = Util.DatatoOBJ(mySurfaceView, mObjList);
        this.pos = new vec(0, 0, 0);
        this.direction = new vec(0, 40, 0);
        this.normal = new vec(0, 0, 0);
    }

    public SkyBox(MySurfaceView mySurfaceView, vec position, vec direction, vec normal){
        List<ObjLoaderUtil.ObjData> mObjList = new ArrayList<>();
        try {
            mObjList.addAll(ObjLoaderUtil.load(ObjPath, mySurfaceView.getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        objs = Util.DatatoOBJ(mySurfaceView, mObjList);
        this.pos = position;
        this.direction = direction;
        this.normal = normal;
    }

    @Override
    public void draw() {
        Log.d("draw", "sky_box");
        MatrixState.pushMatrix();
        MatrixState.scale(1.8f,1.8f,1.8f);
        for(glBasicObj obj:objs){
            obj.drawSelf();
        }
        MatrixState.popMatrix();
    }
}
