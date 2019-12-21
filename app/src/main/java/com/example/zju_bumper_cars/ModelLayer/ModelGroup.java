package com.example.zju_bumper_cars.ModelLayer;

import android.util.Log;

import com.example.zju_bumper_cars.ControlLayer.controlers.player_controler;
import com.example.zju_bumper_cars.ModelLayer.models.BaseModel;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.ModelLayer.models.test_obj;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

public class ModelGroup {

    private static List<BaseModel> modelGroup;
    public static List<Cars> ALLPlayer;
    public static Cars Player;

    public static void initData(MySurfaceView surfaceView){
        Player = new Cars(surfaceView, new vec(-3, 0, 3), new vec(270, 0, 0));
        Cars car1 = new Cars(surfaceView, new vec(3, 0, 3), new vec(270, 0, 0));
        Cars car2 = new Cars(surfaceView, new vec(3, 0, -3), new vec(270, 0, 0));
        Cars car3 = new Cars(surfaceView, new vec(-3, 0, -3), new vec(270, 0, 0));

        test_obj obj = new test_obj(surfaceView, new vec(0, -27, 0), new vec(0, 0, 0), new vec(1, 0, 0));
        ALLPlayer.add(Player);
        ALLPlayer.add(car1);
        ALLPlayer.add(car2);
        ALLPlayer.add(car3);
        modelGroup.addAll(ALLPlayer);
        for(Cars c:ALLPlayer){
            c.driving();
        }

        modelGroup.add(obj);
    }

    public static void initModel(MySurfaceView surfaceView){
        modelGroup = new ArrayList<>();
        ALLPlayer = new ArrayList<>();
        initData(surfaceView);
    }

    public static void draw(MySurfaceView surfaceView){
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -10);
        for(BaseModel model:modelGroup){
            model.draw();
        }
        MatrixState.popMatrix();
        surfaceView.requestRender();
    }

    public static void addModel(){}

    public static void deleteModel(){}

    public static int CollisionDetect(Cars car){
        for(int i = 0; i<ALLPlayer.size(); i++){
            Cars toDetect = ALLPlayer.get(i);
            if(!toDetect.getPos().same(car.getPos())){
                if(car.detectCollistion(toDetect)){
                    Log.i("detection", "collision detected " + i);
                    vec v = new vec(car.Velocity);
                    car.Velocity = toDetect.Velocity;
                    toDetect.Velocity = v;
                    toDetect.RunState = true;
                    return i;
                }
            }
        }
        return -1;
    }
}
