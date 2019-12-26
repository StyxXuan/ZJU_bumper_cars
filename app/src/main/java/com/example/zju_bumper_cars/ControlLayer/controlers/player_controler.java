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
        ModelGroup.CollisionDetect(ModelGroup.Player);
    }

    public static void goStraght(){
        ModelGroup.Player.goStraight();
        ModelGroup.CollisionDetect(ModelGroup.Player);
    }

    public static void goBack(){
        ModelGroup.Player.goBack();
        ModelGroup.CollisionDetect(ModelGroup.Player);
    }

    public static void ChangeSpeede(){}

}
