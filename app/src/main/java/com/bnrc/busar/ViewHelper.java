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

    private double userLng,userLat,userAsl;


    public ViewHelper(Context context,View tag, TextView textView){
        mSensorListener = new SensorListener(context);
        mSensorListener.setSensorListener(this);

        dbUtil = new DbUtil();
        arUtil = new ArUtil(context,dbUtil.lng,dbUtil.lat,dbUtil.asl);
        mTextView = textView;
        mTag = tag;

        //以下部分为测试代码
//        userLng = (double) userLocation.get(0);
//        userLat = (double) userLocation.get(1);
//        userAsl = (double) userLocation.get(2);
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
    public void onGyroScopeChange(double ω, double γ, double ψ) {
        arUtil.calculate(ω,γ,ψ);
        StringBuilder str = new StringBuilder("方向角：" + (int)ω+ "\n倾斜角：" + (int)γ + "\n俯仰角：" + (int)ψ);
        str.append("\nX: "+arUtil.getX() + "\nY: "+arUtil.getY());
//        str.append("\nUserLng: "+userLng+"\nUserLat: "+userLat+"\nUserAsl: "+userAsl);
//        str.append("\nVx: "+Vx+"\nVy: "+Vy+"\nVz: "+Vz);



//        str.append("\nTest γ: "+ γ2 +"\nTest ψ: "+ψ1) ;
        mTextView.setText(str);
        mTag.setX((float) arUtil.getX());
//        mTag.setX((float)azimuth);
        mTag.setY((float) arUtil.getY());


    }
}
