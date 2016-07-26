package com.desmond.demo.box.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.desmond.demo.R;
import com.desmond.demo.box.view.DrugAddView;
import com.desmond.demo.box.view.DrugSettingView;
import com.desmond.squarecamera.CameraActivity;

/**
 * Created by WIN10 on 2016/7/22.
 */
public class DrugAddActivity extends AppCompatActivity {
    private DrugAddView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new DrugAddView(this);
        setContentView(view.getView());

//        setContentView(R.layout.activity_add_drug);
        Toolbar toolbar = view.getToolbar();
        toolbar.setTitle(R.string.toolbar_drug_add_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public void queryDrugAndFinish(String code){
        Intent intent = this.getIntent();
        intent.putExtra("code", code);
        if (getParent() == null) {
            setResult(RESULT_OK, intent);
        } else {
            getParent().setResult(RESULT_OK, intent);
        }

        finish();
    }

    public void scan(){
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            requestForPermission(permission);
        } else {
            startCameraActivity();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != -1) return;

        if (requestCode == 0) {
            String code = data.getStringExtra("code");
            queryDrugAndFinish(code);
        }
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
    }

    private void startCameraActivity() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, 0);
    }
}
