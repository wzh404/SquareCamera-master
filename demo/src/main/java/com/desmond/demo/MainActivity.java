package com.desmond.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.desmond.demo.base.MainView;
import com.desmond.squarecamera.CameraActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainView view = new MainView(this);
        setContentView(view.getView());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        Log.e("Main-Drug", resultCode + " - " + requestCode);
//        super.onActivityResult(requestCode,resultCode,data);
//    }
}
