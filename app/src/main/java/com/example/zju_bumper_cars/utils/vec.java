package com.example.zju_bumper_cars.utils;

public class vec {
    public double x, y, z;

    public vec(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public vec(double x, double y, double z){
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

    public void standardizeXZ(){
        double mid = Math.sqrt(x*x + y*y);
        x = x/mid;
        y = y/mid;
    }

    public double sum(){
        return x+y+z;
    }

    public double AbsSum(){
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public vec dot(vec p){
        return new vec(this.x * p.x, this.y * p.y, this.z * p.z);
    }

    public vec getNormal(){
        vec newVec = new vec(this);
        newVec.standardizeXZ();
        return newVec;
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
            v.y = y *  Math.cos(theta) + z *  Math.sin(theta);
            v.z = -y *  Math.sin(theta) + z *  Math.cos(theta);
            return v;
        }else if(y_r != 0){
            v.x = x * Math.cos(theta) - z *  Math.sin(theta);
            v.y = y;
            v.z = x * Math.sin(theta) - z *  Math.cos(theta);
            return v;
        }else if(z_r != 0){
            v.x = x * Math.cos(theta) + y *  Math.sin(theta);
            v.y = -x * Math.sin(theta) + y *  Math.cos(theta);
            v.z = z;
            return v;
        }
        return this;
    }

    public vec mul(double f){
        return new vec(x*f, y*f, z*f);
    }

    public void standardize(){
        double length = Math.sqrt(x * x + y * y + z * z);
        x = x / length;
        y = y / length;
        z = z / length;
    }
    public Boolean same(vec p){
        return (x == p.x) & (y == p.y) & (z == p.z);
    }
    public Boolean parallel(vec p){
        if(p.x / this.x == p.y / this.y && p.y / this.y == p.z / this.z)
            return true;
        return false;
    }
    public Boolean opposite(vec p){
        if(this.x == -p.x && this.y == -p.y && this.z == -p.z){
            return true;
        }
        return false;
    }

    public String toString(){
        return this.x + " " + this.y + " " + this.z;
    }
}
