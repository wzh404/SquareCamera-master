package com.desmond.demo.box.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.desmond.demo.R;
import com.desmond.demo.base.view.AbstractView;
import com.desmond.demo.base.webview.WebViewActivity;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.view.DrugSettingView;
import com.desmond.demo.common.AppConfig;
import com.google.gson.JsonObject;

/**
 * Created by wangzunhui on 2016/6/11.
 */
public class DrugSettingActivity extends AppCompatActivity {
    private DrugSettingView view;
    private Drug drug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drug = getIntent().getParcelableExtra("drug");

        view = new DrugSettingView(this, drug);
        setContentView(view.getView());

        setSupportActionBar(view.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        view.setToolbarOnBackPressed();
        view.getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
                intent.putExtra("title", drug.getName());
                intent.putExtra("url", AppConfig.WEB_URL + "/manual/" + drug.getCode());
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drug_setting, menu);

        return true;
    }
}
