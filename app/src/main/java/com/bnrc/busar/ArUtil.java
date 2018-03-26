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

    private double mLng = 116.376364;
    private double mLat = 39.98143;
    private double mAsl = 13.66666;


    public  ArUtil(Context context, double azimuth, double pitch, double roll){
        mAzimuth = azimuth;
        mPitch = pitch;
        mRoll = roll;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        Log.d(TAG,"screenWidth: "+screenWidth);
        Log.d(TAG,"screenHeight: "+screenHeight);
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

        return (Math.toDegrees(Math.atan((mLng-usrLng)/(mLat-usrLat)))-mAzimuth)*(screenWidth/α)+screenWidth/2;
    }

    public double calculateY(double pitch){
        mPitch = pitch;
        return (-Math.toDegrees(Math.asin((mAsl-usrAsl)/this.getDistance(mLat,mLng,usrLat,usrLng)))-mPitch)*(screenHeight/φ)+screenHeight/2;

    }

}
