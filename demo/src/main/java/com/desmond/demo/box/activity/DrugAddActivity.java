package com.desmond.demo.box.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.desmond.demo.R;
import com.desmond.demo.box.view.DrugAddView;
import com.desmond.demo.box.view.DrugSettingView;

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
}
