package com.example.zju_bumper_cars.ControlLayer.controlers;

import com.example.zju_bumper_cars.ModelLayer.ModelGroup;

public class player_controler {

    public static void ChangeDerectionLeft(){
        if(ModelGroup.Player.canMove){
            ModelGroup.Player.goLeft();
        }
    }

    public static void ChageDerectionRight(){
        if(ModelGroup.Player.canMove){
            ModelGroup.Player.goRight();
        }
    }

    public static void goStraght(){
        if(ModelGroup.Player.canMove){
            for(int i=0; i<3; i++)
                ModelGroup.Player.goStraight();
        }
    }

    public static void goBack(){
        if(ModelGroup.Player.canMove){
            for(int i=0; i<3; i++)
                ModelGroup.Player.goBack();
        }
    }

}
