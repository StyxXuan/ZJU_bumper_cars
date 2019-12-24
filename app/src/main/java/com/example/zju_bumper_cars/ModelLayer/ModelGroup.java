package com.example.zju_bumper_cars.ModelLayer;

import android.util.Log;
import android.view.animation.AccelerateInterpolator;

import com.example.zju_bumper_cars.ControlLayer.controlers.AI_controler;
import com.example.zju_bumper_cars.ControlLayer.controlers.player_controler;
import com.example.zju_bumper_cars.MainActivity;
import com.example.zju_bumper_cars.ModelLayer.models.BaseModel;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.ModelLayer.models.Particle;
//import com.example.zju_bumper_cars.ModelLayer.models.ParticleSystem;
import com.example.zju_bumper_cars.ModelLayer.models.SkyBox;
import com.example.zju_bumper_cars.ModelLayer.models.test_obj;
import com.example.zju_bumper_cars.R;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.config.glConfig;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.vec;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.List;

public class ModelGroup {
    private static List<BaseModel> modelGroup;
    public static List<Cars> ALLPlayer;
//    public static List<ParticleSystem> particleSystems;
    public static Cars Player;
    public static SkyBox skyBox;
    public static boolean ParticleSystemReady = false;
    public static boolean CollisionHapen = false;
    public static boolean initDown = false;

    public static void initData(MySurfaceView surfaceView){
        Player = new Cars(surfaceView, new vec(-10, 0, 10), new vec(270, 0, 0));
        Player.isPlayer = true;
        Cars car1 = new Cars(surfaceView, new vec(10, 0, 10), new vec(270, 0, 0));
        Cars car2 = new Cars(surfaceView, new vec(10, 0, -10), new vec(270, 0, 0));
        Cars car3 = new Cars(surfaceView, new vec(-10, 0, -10), new vec(270, 0, 0));
        skyBox = new SkyBox(surfaceView);

        test_obj obj = new test_obj(surfaceView, new vec(0, -27, 0), new vec(0, 0, 0), new vec(1, 0, 0));
        ALLPlayer.add(Player);
        ALLPlayer.add(car1);
        ALLPlayer.add(car2);
        ALLPlayer.add(car3);
        modelGroup.addAll(ALLPlayer);
        for(Cars c:ALLPlayer){
            c.driving();
        }
//        for(int i=0; i<2; i++){
//            ParticleSystem part = new ParticleSystem(surfaceView);
//            particleSystems.add(part);
//        }
//        ParticleSystem part = new ParticleSystem(surfaceView, "red.obj", new vec(0, 0, 0), new vec(0, -1, 0));
//        particleSystems.add(part);
        modelGroup.add(obj);
    }

    public static void initModel(MySurfaceView surfaceView){
        modelGroup = new ArrayList<>();
        ALLPlayer = new ArrayList<>();
//        particleSystems = new ArrayList<>();
        initData(surfaceView);
    }

    public static void draw(MySurfaceView surfaceView){
        MatrixState.pushMatrix();

        MatrixState.setLightLocation(glConfig.LIGHT_POS_X, glConfig.LIGHT_POS_Y, glConfig.LIGHT_POS_Z);

        MatrixState.translate(0, 0, -10);
        for(BaseModel model:modelGroup){
            model.draw();
        }

        if(ParticleSystemReady){

//            for(ParticleSystem system:particleSystems){
//                if(system.inUse && system.readyToDraw){
////                    if(system.LifeTime % 2 == 0){
////                        continue;
////                    }
//                    system.drawSelf();
//                    if(system.LifeTime <= 0){
//                        system.inUse = false;
//                        system.readyToDraw = false;
//                    }
//                }
//            }
        }

        MatrixState.popMatrix();
        surfaceView.requestRender();
    }


    public static void addModel(){}

    public static void deleteModel(){}

    public static void CollisionDetect(Cars car){
        for(int i = 0; i<ALLPlayer.size(); i++){
            Cars toDetect = ALLPlayer.get(i);
            if(!toDetect.getPos().same(car.getPos())){
                if(car.detectCollistion(toDetect)){
                    Log.i("detection", "collision detected " + i);
                    toDetect.onCollision = true;
                    CollisionHapen = true;
                    vec v = new vec(car.Velocity);
                    car.Velocity = toDetect.Velocity;
                    car.pos.add(car.getVelocity().mul(0.01f));
                    toDetect.Velocity = v;
                    toDetect.pos.add(toDetect.getVelocity().mul(0.01f));
                    toDetect.addParticleSys();
                    toDetect.RunState = true;
                    car.RunState = true;
                }
            }
        }
    }
}
