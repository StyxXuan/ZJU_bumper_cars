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
    public static vec bouningBox = new vec(1.565f, 3.863f, 1.093f);
    private List<glBasicObj> objs;

    public Cars(MySurfaceView mySurfaceView){
        List<ObjLoaderUtil.ObjData> mObjList = new ArrayList<>();
        try {
            mObjList.addAll(ObjLoaderUtil.load(ObjPath, mySurfaceView.getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        objs = Util.DatatoOBJ(mySurfaceView, mObjList);
        this.pos = new vec(0, 0, 0);
        this.direction = new vec(270, 0, 0);
        this.normal = new vec(0, 1, 0);
    }

    public Cars(MySurfaceView mySurfaceView, vec position, vec direction, vec normal){
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

    public void goStraight(){
        pos.y += 1;
    }

    public void goBack(){
        pos.y -= 1;
    }

    public void goLeft(){
        pos.x += 1;
    }

    public void goRight(){
        pos.x -= 1;
    }

    public Boolean detectCollistion(Cars b){
        vec b_normal = b.getNormal();
        vec b_vertical = b_normal.rotate(90, 0, 0, 1);
        vec RightDownPoint = pos.add(b_normal.mul(1/2)).add(b_vertical.mul(1/2));
        vec LeftDownPoint = pos.add(b_normal.mul(1/2)).sub(b_vertical.mul(1/2));
        vec LeftUpPoint = pos.sub(b_normal.mul(1/2)).add(b_vertical.mul(1/2));
        vec RightUpPoint = pos.sub(b_normal.mul(1/2)).sub(b_vertical.mul(1/2));

        boolean collision = false;
        collision |= this.checkInBox(RightDownPoint);
        collision |= this.checkInBox(LeftDownPoint);
        collision |= this.checkInBox(LeftUpPoint);
        collision |= this.checkInBox(RightUpPoint);
        return collision;
    }

    public boolean checkInBox(vec a){
        boolean res = false;
        vec b = a.sub(pos);
        res |= Math.abs(b.x) < (Cars.bouningBox.x / 2);
        res |= Math.abs(b.y) < (Cars.bouningBox.y / 2);
        res |= Math.abs(b.z) < (Cars.bouningBox.z / 2);
        return res;
    }

    @Override
    public void draw() {
        MatrixState.pushMatrix();
        MatrixState.rotate(direction.x, 1, 0, 0);
        MatrixState.rotate(direction.y, 0, 1, 0);
        MatrixState.rotate(direction.z, 0, 0, 1);
        MatrixState.translate(pos.x, pos.y, pos.z);
        for(glBasicObj obj:objs){
            obj.drawSelf();
        }
        MatrixState.popMatrix();
    }
}
