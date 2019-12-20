package com.example.zju_bumper_cars.utils;

public class vec {
    public float x, y, z;

    public vec(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

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

    public vec dot(vec p){
        return new vec(this.x * p.x, this.y * p.y, this.z * p.z);
    }

    public vec add(vec p){
        return new vec(this.x + p.x, this.y + p.y, this.z + p.z);
    }

    public vec sub(vec p){
        return new vec(this.x - p.x, this.y - p.y, this.z - p.z);
    }

    public vec rotate(double theta, int x_r, int y_r, int z_r){
        vec v = new vec();
        if(x_r != 0){
            v.x = x;
            v.y = y * (float) Math.cos(theta) + z * (float) Math.sin(theta);
            v.z = -y * (float) Math.sin(theta) + z * (float) Math.cos(theta);
            return v;
        }else if(y_r != 0){
            v.x = x * (float) Math.cos(theta) - z * (float) Math.sin(theta);
            v.y = y;
            v.z = x * (float) Math.sin(theta) - z * (float) Math.cos(theta);
            return v;
        }else if(z_r != 0){
            v.x = x * (float) Math.cos(theta) + y * (float) Math.sin(theta);
            v.y = -x * (float) Math.sin(theta) + y * (float) Math.cos(theta);
            v.z = z;
            return v;
        }
        return this;
    }

    public vec mul(float f){
        return new vec(x*f, y*f, z*f);
    }

    public Boolean same(vec p){
        return (x == p.x) & (y == p.y) & (z == p.z);
    }
}
