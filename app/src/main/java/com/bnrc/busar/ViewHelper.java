package com.bnrc.busar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by apple on 2018/3/18.
 */

public class ViewHelper implements SensorListener.ISensorListener{
    private View mTag;
    private TextView mTextView;
    private SensorListener mSensorListener;

    private DbUtil dbUtil;
    private ArUtil arUtil;


    public ViewHelper(Context context,View tag, TextView textView){
        mSensorListener = new SensorListener(context);
        mSensorListener.setSensorListener(this);

        dbUtil = new DbUtil();
        arUtil = new ArUtil(context,dbUtil.lng,dbUtil.lat,dbUtil.asl);
        mTextView = textView;
        mTag = tag;
    }

    /**
     * 注册监听传感器事件
     */
    public void start() {
        mSensorListener.start();
    }

    /**
     * 监听传感器事件耗电，因此在onPause里需要注销监听事件
     */
    public void stop() {
        mSensorListener.stop();
    }


    @Override
    public void onGyroScopeChange(double azimuth, double pitch, double roll) {

        mTextView.setText("Azimuth：" + (int)azimuth + "\nPitch：" + (int)pitch + "\nRoll：" + (int)roll  + "\nX: "+arUtil.calculateX(azimuth) + "\nY: "+arUtil.calculateY(pitch));
        mTag.setX((float) arUtil.calculateX(azimuth));
//        mTag.setX((float)azimuth);
        mTag.setY((float) arUtil.calculateY(pitch));


    }
}
