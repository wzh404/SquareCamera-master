package com.desmond.demo.plan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.desmond.demo.box.model.Drug;
import com.desmond.demo.plan.model.DrugPlan;
import com.desmond.demo.plan.view.DayPlanView;
import com.desmond.demo.plan.view.DrugPlanDetailView;

/**
 * Created by wangzunhui on 2016/6/14.
 */
public class DrugPlanDetailActivity extends AppCompatActivity {
    private DrugPlanDetailView view;
    private DrugPlan plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        drug = getIntent().getParcelableExtra("drug");
        DrugPlan plan = getIntent().getParcelableExtra("plan");
        view = new DrugPlanDetailView(this, plan);
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
