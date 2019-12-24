package com.example.zju_bumper_cars.ModelLayer.models;

import android.graphics.Point;
import android.util.Log;

import com.example.zju_bumper_cars.IOLayer.Obj.ObjLoaderUtil;
import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.Util;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

public class test_obj extends  BaseModel{
    private static String ObjPath = "floor.obj";
    private List<glBasicObj> objs;
    private static Point[] rec1 = {new Point(-36, -4), new Point(-19, -37), new Point(37, -4), new Point(19, 26)};
    private static Point[] rec2 = {new Point(-19, 26), new Point(-36, -4), new Point(19, -37), new Point(37, -4)};

    public test_obj(MySurfaceView mySurfaceView){
        List<ObjLoaderUtil.ObjData> mObjList = new ArrayList<>();
        try {
            mObjList.addAll(ObjLoaderUtil.load(ObjPath, mySurfaceView.getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        objs = Util.DatatoOBJ(mySurfaceView, mObjList);
        this.pos = new vec(0, 0, 0);
        this.direction = new vec(0, 0, 0);
        this.normal = new vec(0, 0, 0);
    }

    public test_obj(MySurfaceView mySurfaceView, vec position, vec direction, vec normal){
        List<ObjLoaderUtil.ObjData> mObjList = new ArrayList<>();
        try {
            mObjList.addAll(ObjLoaderUtil.load(ObjPath, mySurfaceView.getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        objs = Util.DatatoOBJ(mySurfaceView, mObjList);
        this.pos = position;
        this.direction = direction;
        this.normal = normal;
    }

    public static Boolean detectBorder(Cars c){
        return checkInRec(c.pos);
    }
    public static Boolean checkInRec(vec p){
        if(p.z <= 26 && p.z >= -37 && p.x >= -19 && p.x <= 19) {
            Log.d("first rec", "in");
            return true;
        }
        if(GetCross(rec1[0], rec1[1], p.x, p.z) * GetCross(rec1[2], rec1[3], p.x, p.z) >= 0 && GetCross(rec1[1], rec1[2], p.x, p.z) * GetCross(rec1[3], rec1[0], p.x, p.z) >= 0){
            Log.d("second rec", "in");
            return true;
        }
        if(GetCross(rec2[0], rec2[1], p.x, p.z) * GetCross(rec2[2], rec2[3], p.x, p.z) >= 0 && GetCross(rec2[1], rec2[2], p.x, p.z) * GetCross(rec2[3], rec2[0], p.x, p.z) >= 0){
            Log.d("third rec", "in");
            return true;
        }
        Log.d("out", "out of floor");
        return false;
    }
    @Override
    public void draw() {
        Log.d("draw", "obj_test");
        MatrixState.pushMatrix();
        MatrixState.translate((float)pos.x, (float)pos.y, (float)pos.z);
        MatrixState.rotate((float) direction.y, 0, 1, 0);
        MatrixState.rotate((float)direction.z, 0, 0, 1);
        MatrixState.rotate((float)direction.x, 1, 0, 0);
        MatrixState.scale(0.8f, 0.8f, 0.8f);
        for(glBasicObj obj:objs){
            obj.drawSelf();
        }
        MatrixState.popMatrix();
    }

    public static double GetCross(Point p1, Point p2, double x, double y)
    {
        Log.d("cross", "calculate" + x + " " + y);
        return (p2.x - p1.x) * (y - p1.y) -(x - p1.x) * (p2.y - p1.y);
    }
}
