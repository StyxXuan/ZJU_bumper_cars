package com.example.zju_bumper_cars.ModelLayer.models;


import com.example.zju_bumper_cars.utils.MatrixState;

public class cars extends BaseModel{

    private glTextureObj textureObj;
    public void initObj(glTextureObj obj) {this.textureObj = obj;}

    @Override
    public void draw() {
        MatrixState.pushMatrix();
        MatrixState.rotate(pos.x, 1, 0, 0);
        MatrixState.rotate(pos.y, 0, 1, 0);
        MatrixState.rotate(pos.z, 0, 0, 1);
        MatrixState.translate(direction.x, direction.y, direction.z);
        textureObj.drawSelf();
        MatrixState.popMatrix();
    }
}
