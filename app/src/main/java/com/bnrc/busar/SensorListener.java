package com.bnrc.busar;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.net.InterfaceAddress;

/**
 * Created by apple on 2018/3/18.
 */

public class SensorListener implements SensorEventListener {

    public Context context;
    private SensorManager sensorManager;
    private Sensor magneticSensor, accelerometerSensor;
    private float[] values, r, gravity, geomagnetic;

    private ISensorListener sensorListener;

    public SensorListener(Context context){
        this.context = context;
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager == null)
            return;

        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        values = new float[3];//用来保存最终的结果
        gravity = new float[3];//用来保存加速度传感器的值
        r = new float[9];//
        geomagnetic = new float[3];//用来保存地磁传感器的值
    }

    public void setSensorListener(ISensorListener sensorListener) {
        this.sensorListener = sensorListener;
    }


    public void start() {
        if (magneticSensor == null || accelerometerSensor == null)
            return;

        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void stop() {
        if (magneticSensor == null || accelerometerSensor == null)
            return;

        sensorManager.unregisterListener(this);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
            getValue();
        }

    }

    public void getValue() {
        // r从这里返回
        SensorManager.getRotationMatrix(r, null, gravity, geomagnetic);
        //values从这里返回
        SensorManager.getOrientation(r, values);
        //提取数据
        double azimuth = Math.toDegrees(values[0]);
        if (azimuth<0) {
            azimuth=azimuth+360;
        }
        double pitch = Math.toDegrees(values[1]);
        double roll = Math.toDegrees(values[2]);

        if (sensorListener != null)
            sensorListener.onGyroScopeChange(azimuth,pitch,roll);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface ISensorListener{
        void onGyroScopeChange(double azimuth, double pitch, double roll);
    }
}
