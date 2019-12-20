package com.example.zju_bumper_cars.ControlLayer.controlers;

import com.example.zju_bumper_cars.ModelLayer.ModelGroup;

public class player_controler {

    public static void ChangeView(){}

    public static void ChangeDerectionLeft(){
        ModelGroup.Player.goLeft();
        ModelGroup.CollisionDetect(ModelGroup.Player);
    }

    public static void ChageDerectionRight(){
        ModelGroup.Player.goRight();
    }

    public static void goStraght(){
        ModelGroup.Player.goStraight();
    }

    public static void goBack(){
        ModelGroup.Player.goBack();
    }

    public static void ChangeSpeede(){}

}
