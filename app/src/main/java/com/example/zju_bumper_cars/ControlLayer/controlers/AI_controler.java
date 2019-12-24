package com.example.zju_bumper_cars.ControlLayer.controlers;

import android.util.Log;

import com.example.zju_bumper_cars.ModelLayer.ModelGroup;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.utils.vec;

public class AI_controler {

    public static void attack(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                vec RelativeAngle = new vec(0, 0, 0);
                while(ModelGroup.Player.isLive){
                    for(Cars c : ModelGroup.ALLPlayer){
                        if(c != ModelGroup.Player && !c.AimAt(RelativeAngle)){
                            RelativeAngle = ModelGroup.Player.pos.sub(c.pos);
                            RelativeAngle.standardize();
                            c.setRunState(true);
                            c.goLeft();
                        }
                    }
                }
            }
        }.start();

    }
}
