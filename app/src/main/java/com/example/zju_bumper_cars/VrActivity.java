package com.example.zju_bumper_cars;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;


import com.example.zju_bumper_cars.ViewLayer.VrRenderer;
import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

public class VrActivity extends GvrActivity{
    private SensorManager mSensorManager;
    private Sensor mSensor;
    public GvrView gvrView;
    VrRenderer mRender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);
        initializeGvrView();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
    }


    private float timestamp = 0;
    private float angle[] = new float[3];
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float gx = 0,gy = 0,gz = 0;

    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.accuracy != 0) {
                int type = sensorEvent.sensor.getType();
                switch (type) {
                    case Sensor.TYPE_GYROSCOPE:
                        if (timestamp != 0) {
                            final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
                            angle[0] += sensorEvent.values[0] * dT;
                            angle[1] += sensorEvent.values[1] * dT;
                            angle[2] += sensorEvent.values[2] * dT;

                            float anglex = (float) Math.toDegrees(angle[0]);
                            float angley = (float) Math.toDegrees(angle[1]);
                            float anglez = (float) Math.toDegrees(angle[2]);

                            if (gx != 0) {
                                float c = gx - anglex;
                                if (Math.abs(c) >= 0.5) {
                                    Log.d("================", "anglex------------>" + (gx - anglex));
                                    gx = anglex;
                                }

                            } else {
                                gx = anglex;
                            }
                            if (gy != 0) {
                                float c = gy - angley;
                                if (Math.abs(c) >= 0.5) {
                                    Log.d("================", "anglex------------>" + (gy - angley));
                                    gy = angley;
                                }
                            } else {
                                gy = angley;
                            }
                            if(gz != 0){
                                Log.d("================", "anglex------------>" + (gz - anglez));
                            }


                            gz = anglez;

                        }
                        timestamp = sensorEvent.timestamp;
                        break;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public void initializeGvrView() {

        gvrView = (GvrView) findViewById(R.id.gvr_view);
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);

        mRender = new VrRenderer(this, gvrView);
        gvrView.setRenderer(mRender);
        gvrView.setTransitionViewEnabled(true);

        if (gvrView.setAsyncReprojectionEnabled(true)) {
            AndroidCompat.setSustainedPerformanceMode(this, true);
        }

        setGvrView(gvrView);
    }
}
