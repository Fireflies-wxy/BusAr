package com.bnrc.busar;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by apple on 2018/3/26.
 */

public class CameraView {

//    private Camera mCamera;
//    private SurfaceView sv;
//    private SurfaceHolder sh;
//
//    /**
//     * 获取系统相机
//     *
//     * @return
//     */
//    private Camera getcCamera() {
//        Camera camera = null;
//        try {
//            camera = Camera.open();
//        } catch (Exception e) {
//            camera = null;
//        }
//        return camera;
//    }
//
//    /**
//     * 与SurfaceView传播图像
//     */
//    private void showViews(Camera camera, SurfaceHolder holder) {
//        // 预览相机,绑定
//        try {
//            camera.setPreviewDisplay(holder);
//            // 系统相机默认是横屏的，我们要旋转90°
//            camera.setDisplayOrientation(90);
//            // 开始预览
//            camera.startPreview();
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 释放相机的内存
//     */
//    private void clearCamera() {
//
//        // 释放hold资源
//        if (mCamera != null) {
//            // 停止预览
//            mCamera.stopPreview();
//            mCamera.setPreviewCallback(null);
//            // 释放相机资源
//            mCamera.release();
//            mCamera = null;
//        }
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        // 开始预览
//        showViews(mCamera, sh);
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                               int height) {
//        // 重启功能
//        mCamera.stopPreview();
//        showViews(mCamera, sh);
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        // 释放
//        clearCamera();
//    }
}
