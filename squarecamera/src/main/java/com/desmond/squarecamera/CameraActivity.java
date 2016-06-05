package com.desmond.squarecamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;


public class CameraActivity extends AppCompatActivity {
    public static final String TAG = CameraActivity.class.getSimpleName();
    public Handler mHandler;
//    public TessBaseAPI baseApi;

    public AsyncTessBase baseApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.squarecamera__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("扫一扫");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
        setContentView(R.layout.squarecamera__activity_camera);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance(), CameraFragment.TAG)
                    .commit();
        }

//        baseApi = new TessBaseAPI();
//        File appDir = new File(Environment.getExternalStorageDirectory().getPath(), "Boohee");
//        baseApi.init(appDir.toString(), "eng");
        baseApi = new AsyncTessBase();
        baseApi.init();

        mHandler = new Handler();
    }

    public void returnDrugCode(String code) {
        Intent intent = this.getIntent();
        intent.putExtra("code", code);
        if (getParent() == null) {
            setResult(RESULT_OK, intent);
        } else {
            getParent().setResult(RESULT_OK, intent);
        }

        finish();
    }

    public void retryAutoFoucs(){
        CameraFragment cameraFragment = (CameraFragment)getSupportFragmentManager().getFragments().get(0);
        if (cameraFragment != null) {
            cameraFragment.retryAutoFoucs();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        baseApi.end();
    }

    public void onCancel(View view) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void setPreviewImage(Bitmap bitmap){
        CameraFragment cameraFragment = (CameraFragment)getSupportFragmentManager().getFragments().get(0);
        if (cameraFragment != null) {
            cameraFragment.setImageView(bitmap);
        }
    }

//    public void showOcr(String ori, String s){
//        AlertDialog.Builder builder=new AlertDialog.Builder(this);
//        builder.setTitle("批准文号");
//        builder.setMessage("国药准字" + s);
//
//        builder.create().show();
//    }
}
