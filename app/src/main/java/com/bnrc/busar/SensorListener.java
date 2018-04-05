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
    private Sensor magneticSensor, accelerometerSensor,orientationSensor;
    private float[] values, r, gravity, geomagnetic;
    private float Vx,Vy,Vz;
    private double ω,γ,γ1,γ2,ψ,ψ1;

    private double[] temp = new double[2];
    private int rotCounter = 0;

    private ISensorListener sensorListener;

    private double w1,w2;

    public SensorListener(Context context){
        this.context = context;
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager == null)
            return;

        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);


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

        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this,orientationSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stop() {
        if (magneticSensor == null || accelerometerSensor == null|| orientationSensor == null)
            return;

        sensorManager.unregisterListener(this);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            ω = event.values[0];
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;

            Vx = event.values[SensorManager.DATA_X];

            Vy = event.values[SensorManager.DATA_Y];

            Vz = event.values[SensorManager.DATA_Z];

            getValue();
        }

    }

    public void getValue() {
//        // r从这里返回
//        SensorManager.getRotationMatrix(r, null, gravity, geomagnetic);
//        //values从这里返回
//        SensorManager.getOrientation(r, values);
//        //提取数据
//        ω = values[0];
//        ω = Math.toDegrees(values[0]);
        if (ω<0) {
            ω=ω+360;
        }

        γ = Math.toDegrees(Math.atan(Vx/Vy));

        if(Vy>0){
            γ1 = γ;
        }else if(Vy<0){
            γ1 = 180 + γ;
        }

        if(γ>-45&&γ<45){
            ψ = Math.toDegrees(Math.atan(Vz/Vy));
        }else if(γ>135&&γ<225){
            ψ = -Math.toDegrees(Math.atan(Vz/Vy));
        }else if(γ>45&&γ<135){
            ψ = Math.toDegrees(Math.atan(Vz/Vx));
        }else{
            ψ = -Math.toDegrees(Math.atan(Vz/Vx));
        }

        γ2 = γ1*0.05+γ2*0.95;   //倾斜角，反转角
        ψ1 = ψ*0.05+ψ1*0.95;    //俯仰角


//        double pitch = ψ1;
//        double roll = γ2;
//        double pitch = Math.toDegrees(values[1]);
//        double roll = Math.toDegrees(values[2]);

        if (sensorListener != null)
            sensorListener.onGyroScopeChange(ω,γ2,ψ1);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface ISensorListener{
        void onGyroScopeChange(double ω, double γ, double ψ);
    }
}
