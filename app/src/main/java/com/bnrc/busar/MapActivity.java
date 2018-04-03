package com.bnrc.busar;

import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends AppCompatActivity{

    public LocationClient mLocationClient;
    public MyLocationListener myLocationListener;
    // 自定义定位图标
    private BitmapDescriptor mIconLocation;

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private Button button1,button2,button3,button4;
    private TextView textLocation;
    private int tempCode = 0;
    private boolean isFirstLocate = true;
    private double userLng,userLat,userAsl;

    private MyOrientationListener myOrientationListener;

    private Context context;
    private float mCurrentX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        textLocation = findViewById(R.id.text_location);

        this.context = this;

        initView();

        initLocation();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tempCode == 0){
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    tempCode = 1;
                }else{
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    tempCode = 0;
                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mBaiduMap.isTrafficEnabled())
                    mBaiduMap.setTrafficEnabled(true);
                else
                    mBaiduMap.setTrafficEnabled(false);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mBaiduMap.isBaiduHeatMapEnabled())
                    mBaiduMap.setBaiduHeatMapEnabled(true);
                else
                    mBaiduMap.setBaiduHeatMapEnabled(false);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerToMyLocation();
            }
        });
    }

    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            userLng = bdLocation.getLongitude();
            userLat = bdLocation.getLatitude();
            userAsl = bdLocation.getAltitude();

            StringBuilder str = new StringBuilder("\nUserLng: "+userLng+"\nUserLat: "+userLat+"\nUserAsl: "+userAsl);
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                str.append("\nGPS");
            }else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                str.append("\nNetwork");
            }
            textLocation.setText(str);

            navigateTo(bdLocation);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if(!mLocationClient.isStarted())
            mLocationClient.start();

        myOrientationListener.start();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();

        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_normal:
                break;
            case R.id.id_follow:
                break;
            case R.id.id_compass:
                break;
            default:
        }
        return true;
    }

    public void centerToMyLocation(){
        LatLng latLng = new LatLng(userLat,userLng);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }

    public void initView(){
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    public void initLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);

        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.drawable.navi_map_gps_locked);
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL,true,mIconLocation);
        mBaiduMap.setMyLocationConfigeration(config);

        myOrientationListener = new MyOrientationListener(context);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;

            }
        });
//        OverlayOptions option = new MarkerOptions()
//                .position()
//                .icon(mIconLocation);
//        mBaiduMap.addOverlay(option);
    }

    public void navigateTo(BDLocation bdLocation){

        MyLocationData.Builder locationBuilder= new MyLocationData.Builder();
        locationBuilder.accuracy(bdLocation.getRadius());
        locationBuilder.latitude(bdLocation.getLatitude());
        locationBuilder.longitude(bdLocation.getLongitude());
        locationBuilder.direction(mCurrentX);
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);

        if(isFirstLocate){
            LatLng latLng = new LatLng(bdLocation.getLatitude(),
                    bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;

            Toast.makeText(this,bdLocation.getAddrStr(),Toast.LENGTH_SHORT).show();
        }

    }

}
