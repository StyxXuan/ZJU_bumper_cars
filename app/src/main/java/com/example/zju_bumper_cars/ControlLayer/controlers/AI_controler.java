package com.example.zju_bumper_cars.ControlLayer.controlers;

import android.util.Log;

import com.example.zju_bumper_cars.ModelLayer.ModelGroup;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.utils.vec;

public class AI_controler {
    public static void attack(Cars car){
        vec Direc = car.pos.sub(ModelGroup.Player.pos);
        Direc.standardize();
        if(car.normal.dot(Direc).sum() >= 0.8){
            for(int i=0; i<10; i++)
                car.goStraight();
        }else{
            car.goLeft();
        }
    }
}
