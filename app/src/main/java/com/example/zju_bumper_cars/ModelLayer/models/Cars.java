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
import com.google.vr.sdk.base.GvrView;

import java.util.ArrayList;
import java.util.List;

//import static com.example.zju_bumper_cars.ModelLayer.ModelGroup.particleSystems;

public class Cars extends BaseModel{
    private static String ObjPath = "camaro.obj";
    private static float scale = 3;
    public boolean isLive;
    public boolean canMove;
    public static float bounding_box_scale = 1.5f;
    public static vec bouningBox = new vec(1.5f, 3.6f, 0f);
    private List<glBasicObj> objs;
    public boolean RunState;
    public boolean onCollision;
    public boolean isPlayer;
    public boolean reBirth;
    public double FallingTime;
    public List<Particle> particles;
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
        reBirth = false;
        particles = new ArrayList<>();
    }

    public Cars(GvrView mySurfaceView, vec position, vec direction){
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
        reBirth = false;
        particles = new ArrayList<>();
//        initParticles(mySurfaceView);
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
        reBirth = false;
        particles = new ArrayList<>();
        initParticles(mySurfaceView);
    }

    void initParticles(MySurfaceView mySurfaceView){
        vec b_vertical = normal.rotate(Math.toRadians(90), 0, 1, 0);
        Log.d("vertical", "" + b_vertical.x + " " + b_vertical.y + " " + b_vertical.z);
        vec RightDownPoint = this.pos.add(normal.mul(1f*Cars.bouningBox.y)).add(b_vertical.mul(1f*Cars.bouningBox.x));
        vec LeftDownPoint = this.pos.add(normal.mul(-1f*Cars.bouningBox.y)).sub(b_vertical.mul(1f*Cars.bouningBox.x));
        vec LeftUpPoint = this.pos.add(normal.mul(-1f*Cars.bouningBox.y)).add(b_vertical.mul(1f*Cars.bouningBox.x));
        vec RightUpPoint = this.pos.add(normal.mul(1f*Cars.bouningBox.y)).sub(b_vertical.mul(1f*Cars.bouningBox.x));
        RightDownPoint.y = 0;
        LeftDownPoint.y = 0;
        LeftUpPoint.y = 0;
        RightUpPoint.y = 0;
        particles.add(new Particle(mySurfaceView, RightDownPoint, new vec(0, 0, 0), new vec(0, 0, 0)));
        particles.add(new Particle(mySurfaceView, LeftDownPoint, new vec(0, 0, 0), new vec(0, 0, 0)));
        particles.add(new Particle(mySurfaceView, LeftUpPoint, new vec(0, 0, 0), new vec(0, 0, 0)));
        particles.add(new Particle(mySurfaceView, RightUpPoint, new vec(0, 0, 0), new vec(0, 0, 0)));
    }

    void updateParticles(){
        vec b_vertical = normal.rotate(Math.toRadians(90), 0, 1, 0);
        Log.d("vertical", "" + b_vertical.x + " " + b_vertical.y + " " + b_vertical.z);
        Log.d("normal", "" + normal.x + " " + normal.y + " " + normal.z);

        vec RightDownPoint = this.pos.add(normal.mul(bounding_box_scale*Cars.bouningBox.y)).add(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec LeftDownPoint = this.pos.add(normal.mul(-bounding_box_scale*Cars.bouningBox.y)).sub(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec LeftUpPoint = this.pos.add(normal.mul(-bounding_box_scale*Cars.bouningBox.y)).add(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec RightUpPoint = this.pos.add(normal.mul(bounding_box_scale*Cars.bouningBox.y)).sub(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        RightDownPoint.y = 0;
        LeftDownPoint.y = 0;
        LeftUpPoint.y = 0;
        RightUpPoint.y = 0;
        particles.get(0).pos = RightDownPoint;
        particles.get(1).pos = LeftDownPoint;
        particles.get(2).pos = LeftUpPoint;
        particles.get(3).pos = RightUpPoint;
    }

    void setPlayer(){
        isPlayer = true;
    }

    public void setCanMove(Boolean state){this.canMove = state;}

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
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(RunState && canMove) {
                        Log.d("state", "RunState is true");
                        Log.d("state", ""+ Math.abs(Velocity.sum()));
                        pos = pos.add(Velocity.mul(0.01f));
                        Velocity = Velocity.sub(Velocity.mul(0.05f));
                        if (Math.abs(Velocity.AbsSum()) < 0.01) {
                            Velocity = new vec(0, 0, 0);
                            RunState = false;
                        }
                        ModelGroup.CollisionDetect(Cars.this);
                        Boolean outfBound = Judger.detectBorder(Cars.this);
                        Cars.this.canMove = outfBound;
                        Cars.this.isLive = outfBound;
                    }else if(!canMove && !isLive){
                        FallingTime = 0;
                        for(int i=0; i<200; i++){
                            rotateDown();
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        reBirth = true;
                        isLive = true;
                        direction = new vec(270, 0, 0);
                        pos = new vec((Math.random()-0.5)*40, -17.5, (Math.random()-0.5)*40);
                        Velocity = new vec(0, 0, 0);
                        normal = new vec(Math.cos(Math.toRadians(90-direction.y)), 0, Math.sin(Math.toRadians(90-direction.y)));
                    }
                    if(isLive && reBirth){
                        if(pos.y > - 17.5)
                            pos.y -= 0.1;
                        else{
                            reBirth = false;
                            canMove = true;
                        }
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
        vec v = new vec(Velocity);
        v.setY(0);

        pos = pos.add(v.mul(0.01f));
        pos.setY(pos.y - 0.0001 * (FallingTime*FallingTime));
        FallingTime++;
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

        vec RightDownPoint = b_pos.add(b_normal.mul(bounding_box_scale*Cars.bouningBox.y)).add(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec LeftDownPoint = b_pos.add(b_normal.mul(-bounding_box_scale*Cars.bouningBox.y)).sub(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec LeftUpPoint = b_pos.add(b_normal.mul(-bounding_box_scale*Cars.bouningBox.y)).add(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec RightUpPoint = b_pos.add(b_normal.mul(bounding_box_scale*Cars.bouningBox.y)).sub(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        RightDownPoint.y = 0;
        LeftDownPoint.y = 0;
        LeftUpPoint.y = 0;
        RightUpPoint.y = 0;

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

//        b_vertical = normal.rotate(Math.toRadians(90), 0, 1, 0);
//        RightDownPoint = this.pos.add(normal.mul(0.5f*Cars.bouningBox.y)).add(b_vertical.mul(0.5f*Cars.bouningBox.x));
//        LeftDownPoint = this.pos.add(normal.mul(-0.5f*Cars.bouningBox.y)).sub(b_vertical.mul(0.5f*Cars.bouningBox.x));
//        LeftUpPoint = this.pos.add(normal.mul(-0.5f*Cars.bouningBox.y)).add(b_vertical.mul(0.5f*Cars.bouningBox.x));
//        RightUpPoint = this.pos.add(normal.mul(0.5f*Cars.bouningBox.y)).sub(b_vertical.mul(0.5f*Cars.bouningBox.x));
//        RightDownPoint.y = 0;
//        LeftDownPoint.y = 0;
//        LeftUpPoint.y = 0;
//        RightUpPoint.y = 0;
//
//        collision |= b.checkInBox(RightDownPoint);
//        collision |= b.checkInBox(LeftDownPoint);
//        collision |= b.checkInBox(LeftUpPoint);
//        collision |= b.checkInBox(RightUpPoint);

        return collision;
    }

    public boolean checkInBox(vec a){
        boolean res = true;
//        vec b = a.sub(pos);
////        Log.d("point subed", ""+b.x +" "+b.y + " " + b.z);
////        vec b_norm = new vec(normal);
////        vec b_vertical = normal.rotate(Math.toRadians(90), 0, 1, 0);
////        b_norm.standardizeXZ();
////        b_vertical.standardizeXZ();
////        vec pos_orien = a.sub(pos);
////        if(Math.abs(b_norm.x * pos_orien.x + b_norm.z * pos_orien.z) > scale*bouningBox.y * 3/4){
////            res = false;
////        }
////        if(Math.abs(b_vertical.x * pos_orien.x + b_vertical.z * pos_orien.z) > scale*bouningBox.x * 3/4){
////            res = false;
////        }

        vec b_vertical = normal.rotate(Math.toRadians(90), 0, 1, 0);
        vec RightDownPoint = this.pos.add(normal.mul(bounding_box_scale*Cars.bouningBox.y)).add(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec LeftDownPoint = this.pos.add(normal.mul(-bounding_box_scale*Cars.bouningBox.y)).sub(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec LeftUpPoint = this.pos.add(normal.mul(-bounding_box_scale*Cars.bouningBox.y)).add(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec RightUpPoint = this.pos.add(normal.mul(bounding_box_scale*Cars.bouningBox.y)).sub(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        double x_max = Math.max(Math.max(RightDownPoint.x, LeftDownPoint.x), Math.max(LeftUpPoint.x, RightUpPoint.x));
        double x_min = Math.min(Math.min(RightDownPoint.x, LeftDownPoint.x), Math.min(LeftUpPoint.x, RightUpPoint.x));
        double z_min = Math.min(Math.min(RightDownPoint.z, LeftDownPoint.z), Math.min(LeftUpPoint.z, RightUpPoint.z));
        double z_max = Math.min(Math.max(RightDownPoint.z, LeftDownPoint.z), Math.max(LeftUpPoint.z, RightUpPoint.z));

        res &= (a.x <= x_max) && (a.x > x_min);
        res &= (a.z <= z_max) && (a.z > z_min);
        return res;
    }

    @Override
    public void draw() {
        Log.d("Car_Draw", "drawing");
        MatrixState.pushMatrix();
        for(Particle particle : particles){
            particle.draw();
        }
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate((float)pos.x, (float)pos.y, (float)pos.z);
        MatrixState.scale(scale, scale, scale);
        MatrixState.rotate((float) direction.y, 0, 1, 0);
        MatrixState.rotate((float)direction.z, 0, 0, 1);
        MatrixState.rotate((float)direction.x, 1, 0, 0);

        for(glBasicObj obj:objs){
            obj.drawSelf();
        }

        updateParticles();
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
