package com.example.zju_bumper_cars.ModelLayer.models;



import android.media.midi.MidiOutputPort;
import android.util.Log;

import com.example.zju_bumper_cars.ControlLayer.controlers.AI_controler;
import com.example.zju_bumper_cars.ControlLayer.controlers.Judger;
import com.example.zju_bumper_cars.IOLayer.Obj.ObjLoaderUtil;
import com.example.zju_bumper_cars.ModelLayer.ModelGroup;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.Util;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

//import static com.example.zju_bumper_cars.ModelLayer.ModelGroup.particleSystems;

public class Cars extends BaseModel{
    private static String ObjPath = "camaro.obj";
    private static float scale = 3;
    public boolean isLive;
    public boolean canMove;
    public static vec bouningBox = new vec(3.863f*scale, 1.565f*scale, 1.093f*scale*1.5);
    private List<glBasicObj> objs;
    public boolean RunState;
    public boolean onCollision;
    public boolean isPlayer;
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
        isLive = true;
        RunState = false;
        canMove = true;
        onCollision = false;
        isPlayer = false;
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
        isLive = true;
        RunState = false;
        canMove = true;
        onCollision = false;
        isPlayer = false;
    }

    void setPlayer(){
        isPlayer = true;
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
        direction.y -= 0.5;
        normal = new vec(Math.cos(Math.toRadians(90-direction.y)), 0, Math.sin(Math.toRadians(90-direction.y)));
    }

    public void goLeft(){
        direction.y += 0.5;
        normal = new vec(Math.cos(Math.toRadians(90-direction.y)), 0, Math.sin(Math.toRadians(90-direction.y)));
    }

    public void driving(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    if(!ModelGroup.initDown) {
                        try {
                            Thread.sleep(100);
                            continue;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if(!isPlayer){
                        AI_controler.attack(Cars.this);
                    }
                    if(RunState && canMove) {
                        Log.d("state", "RunState is true");
                        Log.d("state", ""+ Math.abs(Velocity.sum()));
                        pos = pos.add(Velocity.mul(0.01f));
                        Velocity = Velocity.sub(Velocity.mul(0.01f));
                        if (Math.abs(Velocity.AbsSum()) < 0.01) {
                            Velocity = new vec(0, 0, 0);
                            RunState = false;
                        }
                        Boolean outfBound = Judger.detectBorder(Cars.this);
                        Cars.this.canMove = outfBound;
                        Cars.this.isLive = outfBound;
                    }else if(!canMove && !isLive){
                        rotateDown();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    private void rotateDown(){
        int dis = -1;
        if(pos.z < -4)
            dis = 1;

        for(int i=0; i<10; i++)
            direction.x += dis;

        direction.x += dis;
        pos.y -= 1;
        pos.add(getVelocity().mul(0.001f));
    }
    public void addParticleSys(){
        new Thread(){
            @Override
            public void run() {
                super.run();
//                for(int i=0; i<particleSystems.size(); i++){
//                    if(!particleSystems.get(i).inUse){
//                        particleSystems.get(i).setParam("red.obj", pos, new vec(0, -1, 0));
//                        Log.d("add ParticleSys", "not inUse");
//                        return;
//                    }
//                }
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
//        vec b = this.pos.sub(a);

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

    public void setRunState(Boolean state){
        this.RunState = state;
    }
    public Boolean AimAt(vec p){

        if(this.normal.opposite(p)){
            Log.d("normal opposite", this.normal + " " + p);
            return true;
        }
        Log.d("normal not opposite", this.normal + " " + p);
        return false;
    }
}
