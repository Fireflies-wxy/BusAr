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
    private double mAzimuth, mPitch, mRoll;
    private double α = 30;
    private double φ = 50;
    private double ω; //方向角
    private double γ; //左右倾斜角
    private double ψ; //前后俯仰角，名字没错

    private double mLng,mLat,mAsl;

    private double x,y,x1,y1;

    private double u,v,u1,v1,r;

    private double angle,angle1; //角坐标θ
//    /**
//     * 宿舍位置
//     */
//    private double userLng=116.362594;
//    private double userLat=39.969452;
//    private double userAsl=1.0;

    /**
     * 实验室位置
     */
    private double userLng=116.365296;
    private double userLat=39.970001;
    private double userAsl=1.0;

    /**
     * 科研楼位置
     */
//    private double userLng=116.365675;
//    private double userLat=39.970208;
//    private double userAsl=1.0;


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

    public double getX(){

        return u;
    }

    public double getY(){

        return v;
    }

    public void calculate(double a1, double a2, double a3){


        this.ω = a1; //方向角
        this.γ = a2; //倾斜角
        this.ψ = a3; //俯仰角

        temp[0] = temp[1];
        temp[1] = ω;

        if(temp[1]-temp[0]>180){
            rotCounter--;
        }

        if(temp[1]-temp[0]<-180){
            rotCounter++;
        }

        ω1 = ω + rotCounter*360;
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

        u = β*(screenWidth/α)+screenWidth/2;
        //u = Math.tan(β/180*Math.PI)*((screenWidth/2)/Math.tan(α/2/180*Math.PI)) + screenWidth/2;

        x = u - screenWidth/2;
        //return Math.tan(β)*((screenWidth/2)/Math.tan(α/2))+screenWidth/2;
        //return β*(screenWidth/α)+screenWidth/2;


        λ = Math.asin((mAsl-userAsl)/this.getDistance(mLat,mLng,userLat,userLng));

        δ = - ψ - λ;

        v = δ*(screenHeight/φ) + screenHeight/2;
        //v=Math.tan(δ/180*Math.PI)*((screenHeight/2)/Math.tan(φ/2/180*Math.PI)) + screenHeight/2;

        y = -v + screenHeight/2;

        //return Math.tan(δ)*((screenHeight/2)/Math.tan(φ/2)) + screenHeight/2;
        //return δ*(screenHeight/φ) + screenHeight/2;

//        r=Math.sqrt(x*x+y*y);
//
//        angle = Math.toDegrees(Math.atan(y/x));
//
//        if(angle>=-180&&angle<0){
//            angle += 360;
//        }
//
//        angle1 = angle - γ;
//
//        x1 = r * Math.cos(angle1);
//
//        y1 = r * Math.sin(angle1);
//
//        u1 = x1+screenWidth/2;
//
//        v1 = -y1-screenHeight/2;

    }


}
