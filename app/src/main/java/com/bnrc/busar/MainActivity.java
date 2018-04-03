package com.bnrc.busar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private Button button_ar,button_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_ar = findViewById(R.id.button_ar);
        button_map = findViewById(R.id.button_map);
        button_ar.setOnClickListener(this);
        button_map.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_ar:
                List<String> permissionList_ar = new ArrayList<>();
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    permissionList_ar.add(Manifest.permission.CAMERA);
                }
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    permissionList_ar.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (!permissionList_ar.isEmpty()) {
                    String [] permissions = permissionList_ar.toArray(new String[permissionList_ar.size()]);
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
                } else{
                    startArActivity();
                }
                break;
            case R.id.button_map:
                List<String> permissionList_map = new ArrayList<>();
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionList_map.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    permissionList_map.add(Manifest.permission.READ_PHONE_STATE);
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    permissionList_map.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (!permissionList_map.isEmpty()) {
                    String [] permissions = permissionList_map.toArray(new String[permissionList_map.size()]);
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, 2);
                } else {
                    startMapActivity();
                }
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    startArActivity();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case 2:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    startMapActivity();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public void startArActivity(){
        Intent intent = new Intent(MainActivity.this,ArActivity.class);
        startActivity(intent);
    }

    public void startMapActivity(){
        Intent intent = new Intent(MainActivity.this,MapActivity.class);
        startActivity(intent);
    }
}
