package com.desmond.demo.plan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.util.DateUtil;
import com.desmond.demo.plan.model.DrugPlan;
import com.desmond.demo.plan.view.NewPlanView;

import java.util.Date;

/**
 * Created by wangzunhui on 2016/6/14.
 */
public class NewPlanActivity extends AppCompatActivity {
    private NewPlanView view;
    private Drug drug;
    private DrugPlan plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drug = getIntent().getParcelableExtra("drug");

        plan = new DrugPlan();
        plan.setId(System.currentTimeMillis());

        plan.setDrug(drug);
        plan.setUser("self");

        plan.setInterval("everyday");
        plan.setDefaultDosageOfDay(drug.getDosage());

        plan.setStartDate(new Date());
        plan.setDays(7);
        plan.setCloseDate(DateUtil.addDate(plan.getStartDate(), 7));

        view = new NewPlanView(this, drug, plan);
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

    public void startDayPlan(String dosages){
        Intent intent = new Intent(this, DayPlanActivity.class);
        intent.putExtra("drug", drug);
        intent.putExtra("dosages", dosages);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 0 && resultCode == 9){
            Log.e("Drug", "************" + data.getStringExtra("dosages"));
            plan.setDosages(data.getStringExtra("dosages"));
        }
    }
}
