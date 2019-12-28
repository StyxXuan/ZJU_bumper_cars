package com.example.zju_bumper_cars.ControlLayer.controlers;

import android.util.Log;

import com.example.zju_bumper_cars.ModelLayer.ModelGroup;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.utils.vec;

public class AI_controler {
    public static void attack(Cars car){
//        vec targetPos = new vec((Math.random()-0.5)*40, 0, (Math.random()-0.5)*40);
//        vec Direc = car.pos.sub(targetPos);
        for(int i=0 ;i <10; i++){
            if(car.canMove && ModelGroup.Player.isLive){
                car.goStraight();
                vec Direc = car.pos.sub(ModelGroup.Player.pos.sub(ModelGroup.Player.normal.mul(2)));
                Direc.standardize();
                if(car.normal.dot(Direc).sum() >= 0.8){
                    car.goStraight();
                }else{
                    car.goLeft();
                }
            }
        }

    }
}
