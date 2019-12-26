package com.example.zju_bumper_cars.ControlLayer.controlers;

import com.example.zju_bumper_cars.ModelLayer.ModelGroup;

public class player_controler {

    public static void ChangeView(){}

    public static void ChangeDerectionLeft(){
        if(ModelGroup.Player.canMove){
            ModelGroup.Player.goLeft();
            ModelGroup.CollisionDetect(ModelGroup.Player);
        }
    }

    public static void ChageDerectionRight(){
        if(ModelGroup.Player.canMove){
            ModelGroup.Player.goRight();
            ModelGroup.CollisionDetect(ModelGroup.Player);
        }
    }

    public static void goStraght(){
        if(ModelGroup.Player.canMove){
            ModelGroup.Player.goStraight();
            ModelGroup.CollisionDetect(ModelGroup.Player);
        }
    }

    public static void goBack(){
        if(ModelGroup.Player.canMove){
            ModelGroup.Player.goBack();
            ModelGroup.CollisionDetect(ModelGroup.Player);
        }
    }

    public static void ChangeSpeede(){}

}
