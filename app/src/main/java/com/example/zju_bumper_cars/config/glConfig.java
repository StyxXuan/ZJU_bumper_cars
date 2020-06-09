package com.example.zju_bumper_cars.config;

public class glConfig {

    private static final String TAG = glConfig.class.getSimpleName();

    /**
     * near far
     */
    public static final float PROJECTION_NEAR = 2;
    public static final float PROJECTION_FAR = 1000;

    /**
     * camera position
     */
    public static float EYE_X = 0f;
    public static float EYE_Y = -1f;
    public static float EYE_Z = 0f;
    public static float VIEW_CENTER_X = 0f;
    public static float VIEW_CENTER_Y = -0.8f;
    public static float VIEW_CENTER_Z = -1f;
    public static float LIGHT_POS_X = 0f;
    public static float LIGHT_POS_Y = 100f;
    public static float LIGHT_POS_Z = 0f;
    public static int COLLISTION_X = 0;
    public static int COLLISTION_Y = 0;
    public static int COLLISTION_Z = 0;
    public static float rotateX = 0.0f;
    public static float rotateY = 0.0f;
    public static float rotateZ = 0.0f;
    public static float angle = 35;
    public static float distance = -10;
    public static double LIGHT_DISPLACEMENT = 0.00005;
}
