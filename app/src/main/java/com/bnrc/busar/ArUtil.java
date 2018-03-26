package com.bnrc.busar;

import android.content.Context;
import android.location.Location;
import android.util.DisplayMetrics;
import android.util.EventLogTags;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by apple on 2018/3/19.
 */

public class ArUtil {
    private static final String TAG = "ArUtil";

    private int screenWidth, screenHeight;
    private double mAzimuth, mPitch, mRoll;
    private double α = 30;
    private double φ = 50;
    private double usrLat = 39.964173;
    private double usrLng = 116.35878;
    private double usrAsl = 13.66666;

    private double mLng,mLat,mAsl;

    /**
     * 计算x相关参数
     */
    private double[] temp = new double[2];
    private int rotCounter = 0;
    private double ω1,ω2;
    private double θ;
    private double β;


    public  ArUtil(Context context, double mLng, double mLat, double mAsl){
//        mAzimuth = azimuth;
//        mPitch = pitch;
//        mRoll = roll;
        this.mLng = mLng;
        this.mLat = mLat;
        this.mAsl = mAsl;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;   //width:1080
        screenHeight = dm.heightPixels; //height:1920
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2){
        float[] results = new float[1];
        try{
            Location.distanceBetween(lat1,lon1,lat2,lon2,results);
        }catch (Exception e){
            e.printStackTrace();
        }
        return results[0];
    }

    public double calculateX(double azimuth){

        mAzimuth = azimuth;

        temp[0] = temp[1];
        temp[1] = mAzimuth;

        if(temp[1]-temp[0]>180){
            rotCounter--;
        }

        if(temp[1]-temp[0]<-180){
            rotCounter++;
        }

        ω1 = azimuth + rotCounter*360;

        ω2 = ω1*0.05+ω2*0.95;

        θ = Math.toDegrees(Math.atan((mLng-usrLng)/(mLat-usrLat)));

        θ = θ + rotCounter*360;

        β = θ - ω2;

        if(β<-180){
            β = β+360;
        }
        if(β>180){
            β = β-360;
        }

        return β*(screenWidth/30)+screenWidth/2;
    }

    public double calculateY(double pitch){
        mPitch = pitch;
        return (-Math.toDegrees(Math.asin((mAsl-usrAsl)/this.getDistance(mLat,mLng,usrLat,usrLng)))-mPitch)*(screenHeight/φ)+screenHeight/2;

    }

}
