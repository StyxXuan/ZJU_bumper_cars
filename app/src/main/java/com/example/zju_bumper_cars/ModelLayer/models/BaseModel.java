package com.example.zju_bumper_cars.ModelLayer.models;

import com.example.zju_bumper_cars.utils.vec;

public abstract class BaseModel {
    public abstract void draw();

    public void setPos(vec pos){
        this.pos = pos;
    }

    public vec getPos() {
        return pos;
    }

    public void setVelocity(float velocity) {
        Velocity = velocity;
    }

    public float getVelocity() {
        return Velocity;
    }

    public vec getDirection() {
        return direction;
    }

    public vec getNormal() {
        return normal;
    }

    public void setDirection(vec direction) {
        this.direction = direction;
    }

    public void setNormal(vec normal) {
        this.normal = normal;
    }

    public vec pos;
    public vec normal;
    public vec direction;
    public float Velocity;
}
