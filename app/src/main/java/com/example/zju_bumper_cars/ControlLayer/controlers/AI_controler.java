package com.example.zju_bumper_cars.ControlLayer.controlers;

import android.util.Log;

import com.example.zju_bumper_cars.ModelLayer.ModelGroup;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;

public class AI_controler {
    public static void attack() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true) {
                    for (Cars c : ModelGroup.ALLPlayer) {
                        if (c != ModelGroup.Player && c.pos != ModelGroup.Player.pos) {
                            c.setRunState(true);
                            c.goRight();
                        }
                    }
                }
            }
        }.start();

    }
}