package com.example.zju_bumper_cars.ModelLayer.models;

import com.example.zju_bumper_cars.IOLayer.Obj.ObjLoaderUtil;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.Util;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

public class test_obj extends  BaseModel{
    private static String ObjPath = "pikachu.obj";
    private List<glBasicObj> objs;

    public test_obj(MySurfaceView mySurfaceView){
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

    public test_obj(MySurfaceView mySurfaceView, vec position, vec direction, vec normal){
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
        MatrixState.pushMatrix();
        MatrixState.rotate(direction.x, 1, 0, 0);
        MatrixState.rotate(direction.y, 0, 1, 0);
        MatrixState.rotate(direction.z, 0, 0, 1);
        MatrixState.translate(pos.x, pos.y, pos.z);
        MatrixState.scale(0.05f, 0.05f, 0.05f);
        for(glBasicObj obj:objs){
            obj.drawSelf();
        }
        MatrixState.popMatrix();
    }
}
