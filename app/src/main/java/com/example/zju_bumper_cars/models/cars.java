package com.example.zju_bumper_cars.models;

import com.example.zju_bumper_cars.utils.Position;

public class cars {
    private Position position;
    private Boolean isPlayer;

    public Position getPosition() {
        return position;
    }

    public void setPlayer(Boolean player) {
        isPlayer = player;
    }

    public void setPosition(Position position) {
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

    public void moveTo(Position position){
        this.position = position;
    }
}
