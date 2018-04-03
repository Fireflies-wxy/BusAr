//package com.bnrc.busar;
//
//import android.content.Context;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.mapapi.map.MyLocationConfiguration;
//
///**
// * Created by apple on 2018/4/1.
// */
//
//public class BaiduMapHelper implements BDLocationListener{
//
//    public BaiduMapHelper(Context context){
//        LocationClient mLocationClient = new LocationClient(context.getApplicationContext());
//        mLocationClient.registerLocationListener(this);
//    }
//
//    @Override
//    public void onReceiveLocation(BDLocation bdLocation) {
//        bdLocation.getLongitude();
//        bdLocation.getLatitude();
//        bdLocation.getAltitude();
//    }
//
//    public double getUserLongitude(BDLocation bdLocation){
//        return bdLocation.getLatitude();
//    }
//
//    public double getUserLatitude(BDLocation bdLocation){
//        return bdLocation.getLatitude();
//    }
//
//    public double getUserAltitude(BDLocation bdLocation){
//        return bdLocation.getAltitude();
//    }
//
//
//}
