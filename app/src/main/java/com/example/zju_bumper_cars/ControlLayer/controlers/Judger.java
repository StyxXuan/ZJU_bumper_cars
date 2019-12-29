package com.example.zju_bumper_cars.ControlLayer.controlers;

import android.graphics.Point;
import android.util.Log;

import com.example.zju_bumper_cars.ModelLayer.models.Cars;
import com.example.zju_bumper_cars.utils.vec;

public class Judger {
    private static Point[] rec1 = {new Point(-45, -15), new Point(-24, -25), new Point(45, -15), new Point(23, 27)};
    private static Point[] rec2 = {new Point(-23, 27), new Point(-45, -15), new Point(23, -50), new Point(45, -15)};
    public static void OutofBound(){

    }
    public static Boolean detectBorder(Cars c){
        return checkInRec(c.pos);
    }

    public static double GetCross(Point p1, Point p2, double x, double y)
    {
        Log.d("cross", "calculate" + x + " " + y);
        return (p2.x - p1.x) * (y - p1.y) -(x - p1.x) * (p2.y - p1.y);
    }

    public static Boolean checkInRec(vec p){
        if(p.z <= 27 && p.z >= -50 && p.x >= -24 && p.x <= 24) {
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
}
