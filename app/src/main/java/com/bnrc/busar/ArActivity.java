package com.bnrc.busar;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private static final String TAG = "ArActivity";

    private Camera mCamera;
    private SurfaceView sv;
    private SurfaceHolder sh;
    private ImageView tag;
    private TextView textView;
    private List userLocation= new ArrayList();
    private LocationClient mLocationClient;

    ViewHelper viewHelper;

    private double userLng,userLat,userAsl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ar);


//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(this);
//        initLocation();
//        mLocationClient.start();

        sv = findViewById(R.id.sv);
        // 初始化SurfaceHolder
        sh = sv.getHolder();
        sh.addCallback(this);

        tag = findViewById(R.id.tag);
        textView = findViewById(R.id.text);
        //viewHelper = new ViewHelper(this,textView);
        viewHelper = new ViewHelper(this,tag,textView);

    }

//    public void initLocation(){
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setCoorType("bd0911");
//        option.setScanSpan(1000);
//        option.setOpenGps(true);
//        option.setLocationNotify(true);
//        option.setIgnoreKillProcess(false);
//        mLocationClient.setLocOption(option);
//    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 在activity运行时绑定
        if (mCamera == null) {
            mCamera = getcCamera();
            if (sh != null) {
                showViews(mCamera, sh);
            }
        }

        //sensor注册
        viewHelper.start();
    }

    @Override
    protected void onPause() {

        // TODO Auto-generated method stub
        super.onPause();
        // activity暂停时我们释放相机内存
        clearCamera();
        viewHelper.stop();
    }

    /**
         * 获取系统相机
         *
         * @return
         */
    private Camera getcCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    /**
     * 与SurfaceView传播图像
     */
    private void showViews(Camera camera, SurfaceHolder holder) {
        // 预览相机,绑定
        try {
            camera.setPreviewDisplay(holder);
            // 系统相机默认是横屏的，我们要旋转90°
            camera.setDisplayOrientation(90);
            // 开始预览
            camera.startPreview();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 释放相机的内存
     */
    private void clearCamera() {

        // 释放hold资源
        if (mCamera != null) {
            // 停止预览
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            // 释放相机资源
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 开始预览
        showViews(mCamera, sh);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // 重启功能
        mCamera.stopPreview();
        showViews(mCamera, sh);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 释放
        clearCamera();
    }

//    @Override
//    public void onReceiveLocation(BDLocation bdLocation) {
//        userLocation.set(0,bdLocation.getLongitude());
//        userLocation.set(1,bdLocation.getLatitude());
//        userLocation.set(2,bdLocation.getAltitude());
//    }
}
