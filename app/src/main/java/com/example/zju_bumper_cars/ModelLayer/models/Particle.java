package com.example.zju_bumper_cars.ModelLayer.models;

import android.util.Log;

import com.example.zju_bumper_cars.IOLayer.Obj.ObjLoaderUtil;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.Util;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

public class Particle extends BaseModel {
    private static String ObjPath = "red.obj";
    private List<glBasicObj> objs;

    public Particle(MySurfaceView mySurfaceView){
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

    public Particle(MySurfaceView mySurfaceView, vec position, vec direction, vec normal){
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

    public void setObjPath(String ObjPath){
        this.ObjPath = ObjPath;
    }

    @Override
    public void draw() {
        MatrixState.pushMatrix();
        MatrixState.translate((float)pos.x, (float)pos.y, (float)pos.z);
        MatrixState.scale(5, 5, 5);
        for(glBasicObj obj:objs){
            obj.drawSelf();
        }
        MatrixState.popMatrix();
    }
}
