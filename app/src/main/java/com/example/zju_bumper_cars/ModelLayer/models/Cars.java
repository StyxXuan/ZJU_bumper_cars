package com.example.zju_bumper_cars.ModelLayer.models;



import android.util.Log;

import com.example.zju_bumper_cars.ControlLayer.controlers.player_controler;
import com.example.zju_bumper_cars.IOLayer.Obj.ObjLoaderUtil;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.Util;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

public class Cars extends BaseModel{
    private static String ObjPath = "camaro.obj";
    private static float scale = 3;
    public static vec bouningBox = new vec(1.565f*scale, 3.863f*scale, 1.093f*scale);
    private List<glBasicObj> objs;
    public boolean RunState;
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
        this.normal = new vec(0, 0, 1);
        Velocity = new vec(0, 0,0);
        RunState = false;
    }

    public Cars(MySurfaceView mySurfaceView, vec position, vec direction){
        List<ObjLoaderUtil.ObjData> mObjList = new ArrayList<>();
        try {
            mObjList.addAll(ObjLoaderUtil.load(ObjPath, mySurfaceView.getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        objs = Util.DatatoOBJ(mySurfaceView, mObjList);
        this.pos = position;
        this.direction = direction;
        this.normal = new vec(Math.cos(Math.toRadians(90-direction.y)), 0, Math.sin(Math.toRadians(90-direction.y)));
        Velocity = new vec(0, 0,0);
        RunState = false;
    }

    public void goBack(){
        Velocity = Velocity.add(normal.mul(0.1f));
        RunState = true;
//        pos = pos.sub(normal.mul(0.1f));
    }

    public void goStraight(){
        Velocity = Velocity.sub(normal.mul(0.1f));
        RunState = true;
//        pos = pos.add(normal.mul(0.1f));
    }

    public void goRight(){
        direction.y -= 1;
        normal = new vec(Math.cos(Math.toRadians(90-direction.y)), 0, Math.sin(Math.toRadians(90-direction.y)));
    }

    public void goLeft(){
        direction.y += 1;
        normal = new vec(Math.cos(Math.toRadians(90-direction.y)), 0, Math.sin(Math.toRadians(90-direction.y)));
    }

    public void driving(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    if(RunState) {
                        Log.d("state", "RunState is true");
                        Log.d("state", ""+ Math.abs(Velocity.sum()));
                        pos = pos.add(Velocity.mul(0.01f));
                        Velocity = Velocity.sub(Velocity.mul(0.01f));
                        if (Math.abs(Velocity.AbsSum()) < 0.01) {
                            Velocity = new vec(0, 0, 0);
                            RunState = false;
                        }
                    }
                }
            }
        }.start();
    }

    public Boolean detectCollistion(Cars b){
        vec b_normal = b.getNormal();
        vec b_vertical = b_normal.rotate(Math.toRadians(90), 0, 1, 0);
        vec b_pos = b.pos;
        Log.d("point b_vertical", ""+b_vertical.x +" "+b_vertical.y + " " + b_vertical.z);
        Log.d("point b_position", ""+b_pos.x +" "+b_pos.y + " " + b_pos.z);

        vec RightDownPoint = b_pos.add(b_normal.mul(0.5f*Cars.bouningBox.y)).add(b_vertical.mul(0.5f*Cars.bouningBox.x));
        vec LeftDownPoint = b_pos.add(b_normal.mul(0.5f*Cars.bouningBox.y)).sub(b_vertical.mul(0.5f*Cars.bouningBox.x));
        vec LeftUpPoint = b_pos.sub(b_normal.mul(0.5f*Cars.bouningBox.y)).add(b_vertical.mul(0.5f*Cars.bouningBox.x));
        vec RightUpPoint = b_pos.sub(b_normal.mul(0.5f*Cars.bouningBox.y)).sub(b_vertical.mul(0.5f*Cars.bouningBox.x));

        Log.d("point RightDownPoint", ""+RightDownPoint.x +" "+RightDownPoint.y + " " + RightDownPoint.z);
        Log.d("point LeftDownPoint", ""+LeftDownPoint.x +" "+LeftDownPoint.y + " " + LeftDownPoint.z);
        Log.d("point LeftUpPoint", ""+LeftUpPoint.x +" "+LeftUpPoint.y + " " + LeftUpPoint.z);
        Log.d("point RightUpPoint", ""+RightUpPoint.x +" "+RightUpPoint.y + " " + RightUpPoint.z);

        Log.d("point Position", ""+pos.x +" "+pos.y + " " + pos.z);
        boolean collision = false;
        collision |= this.checkInBox(RightDownPoint);
        collision |= this.checkInBox(LeftDownPoint);
        collision |= this.checkInBox(LeftUpPoint);
        collision |= this.checkInBox(RightUpPoint);
        return collision;
    }

    public boolean checkInBox(vec a){
        boolean res = true;
        vec b = a.sub(pos);
        Log.d("point subed", ""+b.x +" "+b.y + " " + b.z);
        res &= Math.abs(b.x) <= (Cars.bouningBox.x / 2);
        res &= Math.abs(b.y) <= (Cars.bouningBox.y / 2);
        res &= Math.abs(b.z) <= (Cars.bouningBox.z / 2);
        return res;
    }

    @Override
    public void draw() {
        MatrixState.pushMatrix();
        MatrixState.translate((float)pos.x, (float)pos.y, (float)pos.z);
        MatrixState.rotate((float) direction.y, 0, 1, 0);
        MatrixState.rotate((float)direction.z, 0, 0, 1);
        MatrixState.rotate((float)direction.x, 1, 0, 0);
        MatrixState.scale(scale, scale, scale);
        for(glBasicObj obj:objs){
            obj.drawSelf();
        }
        MatrixState.popMatrix();
    }
}
