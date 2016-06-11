package com.desmond.demo.box.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.view.DrugSettingView;
import com.google.gson.JsonObject;

/**
 * Created by wangzunhui on 2016/6/11.
 */
public class DrugSettingActivity extends AppCompatActivity {
    private DrugSettingView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drug drug = getIntent().getParcelableExtra("drug");

        view = new DrugSettingView(this, drug);
        setContentView(view.getView());

        setSupportActionBar(view.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        view.setToolbarOnBackPressed();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
