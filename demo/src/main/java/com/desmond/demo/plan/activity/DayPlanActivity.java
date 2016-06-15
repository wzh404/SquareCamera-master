package com.desmond.demo.plan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.desmond.demo.box.model.Drug;
import com.desmond.demo.plan.view.DayPlanView;
import com.desmond.demo.plan.view.NewPlanView;

/**
 * Created by wangzunhui on 2016/6/14.
 */
public class DayPlanActivity extends AppCompatActivity {
    private DayPlanView view;
    private Drug drug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drug = getIntent().getParcelableExtra("drug");
        String times = getIntent().getStringExtra("dosages");
        view = new DayPlanView(this, drug, times);
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
