package com.bnrc.busar;

import android.content.Context;
import android.location.Location;
import android.util.DisplayMetrics;
import android.util.EventLogTags;
import android.util.Log;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import java.util.List;

/**
 * Created by apple on 2018/3/19.
 */

public class ArUtil{
    private static final String TAG = "ArUtil";

    private int screenWidth, screenHeight;
    private int mAzimuth, mPitch, mRoll;
    private double α = 30;
    private double φ = 50;
    private double γ; //左右倾斜角
    private double ψ; //前后俯仰角

    private double mLng,mLat,mAsl;
    private double userLng=116.362594;
    private double userLat=39.969452;
    private double userAsl=1.0;

    /**
     * 计算x相关参数
     */
    private double[] temp = new double[2];
    private int rotCounter = 0;
    private double ω1,ω2;
    private double θ;
    private double β;

    /**
     * 计算y相关参数
     */
    private double λ;
    private double δ;


    public  ArUtil(Context context, double mLng, double mLat, double mAsl){
//        mAzimuth = azimuth;
//        mPitch = pitch;
//        mRoll = roll;
        this.mLng = mLng;
        this.mLat = mLat;
        this.mAsl = mAsl;

//        userLng = (double) userLocation.get(0);
//        userLat = (double) userLocation.get(1);
//        userAsl = (double) userLocation.get(2);

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
        
        double result = 0;

        mAzimuth = (int) azimuth;

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

        θ = Math.toDegrees(Math.atan((mLng-userLng)/(mLat-userLat)));

        θ = θ + rotCounter*360;

        β = θ - ω2;

        if(β<-180){
            β = β+360;
        }
        if(β>180){
            β = β-360;
        }
        //return Math.tan(β)*((screenWidth/2)/Math.tan(α/2))+screenWidth/2;
        return β*(screenWidth/α)+screenWidth/2;

    }

    public double calculateY(double pitch){

        mPitch = (int) ((pitch+90)*0.05+mPitch*0.95);

        λ = Math.asin((mAsl-userAsl)/this.getDistance(mLat,mLng,userLat,userLng));

        δ = -mPitch - λ;

        //return Math.tan(δ)*((screenHeight/2)/Math.tan(φ/2)) + screenHeight/2;
        return δ*(screenHeight/φ) + screenHeight/2;
    }

}
