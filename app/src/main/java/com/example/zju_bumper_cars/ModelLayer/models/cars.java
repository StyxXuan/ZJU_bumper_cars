package com.example.zju_bumper_cars.ModelLayer.models;


import com.example.zju_bumper_cars.utils.vec;

public class cars {
    private vec position;
    private Boolean isPlayer;

    public vec getPosition() {
        return position;
    }

    public void setPlayer(Boolean player) {
        isPlayer = player;
    }

    public void setPosition(vec position) {
        this.position = position;
    }

    public Boolean getPlayer() {
        return isPlayer;
    }

    public cars(){

    }

    public void init(){

    }

    public void draw(){

    }

    public void moveTo(vec position){
        this.position = position;
    }
}
