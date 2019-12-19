package com.example.zju_bumper_cars.utils;

public class vec {
    public float x, y, z;
    public vec(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public vec(vec p){
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public vec(float []pos){
        this.x = pos[0];
        this.y = pos[1];
        this.z = pos[2];
    }


    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
