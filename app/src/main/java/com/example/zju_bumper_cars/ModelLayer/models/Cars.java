package com.example.zju_bumper_cars.ModelLayer.models;


import com.example.zju_bumper_cars.IOLayer.Obj.ObjLoaderUtil;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.Util;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

public class Cars extends BaseModel{
    private static String ObjPath = "camaro.obj";
    private List<glBasicObj> objs;

    public Cars(MySurfaceView mySurfaceView){
        List<ObjLoaderUtil.ObjData> mObjList = new ArrayList<>();
        try {
            mObjList.addAll(ObjLoaderUtil.load("camaro.obj", mySurfaceView.getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        objs = Util.DatatoOBJ(mySurfaceView, mObjList);
        this.pos = new vec(0, 0, 0);
        this.direction = new vec(0, 0, 0);
        this.normal = new vec(0, 0, 0);
    }

    public Cars(MySurfaceView mySurfaceView, vec position, vec direction, vec normal){
        List<ObjLoaderUtil.ObjData> mObjList = new ArrayList<>();
        try {
            mObjList.addAll(ObjLoaderUtil.load("camaro.obj", mySurfaceView.getResources()));
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
        MatrixState.pushMatrix();
        MatrixState.rotate(pos.x, 1, 0, 0);
        MatrixState.rotate(pos.y, 0, 1, 0);
        MatrixState.rotate(pos.z, 0, 0, 1);
        MatrixState.translate(direction.x, direction.y, direction.z);
        for(glBasicObj obj:objs){
            obj.drawSelf();
        }
        MatrixState.popMatrix();
    }
}
