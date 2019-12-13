package com.example.zju_bumper_cars.utils;

public class vec {
    private float x, y, z;
    public vec(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public vec(vec p){
        this.x = p.getX();
        this.y = p.getY();
        this.z = p.getZ();
    }

    public vec(float []pos){
        this.x = pos[0];
        this.y = pos[1];
        this.z = pos[2];
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
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
