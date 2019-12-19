package com.example.zju_bumper_cars.ModelLayer;

import android.util.Log;

import com.example.zju_bumper_cars.ModelLayer.map.Constant;
import com.example.zju_bumper_cars.ModelLayer.models.BaseModel;
import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.ModelLayer.models.Mountion;
import com.example.zju_bumper_cars.R;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

public class ModelGroup {

    private static List<BaseModel> modelGroup;

    public static void initData(MySurfaceView surfaceView){
        Cars car = new Cars(surfaceView);
        Cars car2 = new Cars(surfaceView, new vec(3, 3, 0), new vec(40, 20, 0), new vec(10, 20, 30));

        modelGroup.add(car);
        modelGroup.add(car2);
    }

    public static void initModel(MySurfaceView surfaceView){
        modelGroup = new ArrayList<>();
        initData(surfaceView);
    }

    public static void draw(MySurfaceView surfaceView){
        Log.d("info", "draw objs");
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -10);
        for(BaseModel model:modelGroup){
            Log.d("info", "draw model");
            model.draw();
        }
        MatrixState.popMatrix();
        surfaceView.requestRender();
    }

    public static void addModel(){}

    public static void deleteModel(){}
}
