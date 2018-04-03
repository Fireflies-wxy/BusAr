package com.bnrc.busar;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.session.MediaSessionManager;

import com.baidu.mapapi.map.MyLocationConfiguration;

import java.io.InputStream;

/**
 * Created by apple on 2018/4/2.
 */

public class MyOrientationListener implements SensorEventListener {
    private SensorManager mSensorManager;
    private Context mContext;
    private Sensor mSensor;
    private OnOrientationListener mOnOrientationListener;

    private float lastX;

    public MyOrientationListener(Context context){
        this.mContext = context;

    }

    public void start(){
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager!=null){
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }

        if(mSensor!=null){
            mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ORIENTATION){
            float x = sensorEvent.values[SensorManager.DATA_X];

            if(Math.abs(x-lastX)>1.0){
                if(mOnOrientationListener!=null){
                    mOnOrientationListener.onOrientationChanged(x);
                }
            }

            lastX = x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void setOnOrientationListener(OnOrientationListener mOnOrientationListener) {
        this.mOnOrientationListener = mOnOrientationListener;
    }

    public interface OnOrientationListener
    {
        void onOrientationChanged(float x);
    }
}
