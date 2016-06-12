package com.desmond.demo.box.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.base.adapter.DefaultItemRecyclerAdapter;
import com.desmond.demo.base.view.AbstractRecyclerView;
import com.desmond.demo.base.view.AbstractSwipeRefresh;
import com.desmond.demo.base.view.AbstractView;
import com.desmond.demo.box.adapter.DrugRecyclerAdapter;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.util.AndroidUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugSettingView extends AbstractRecyclerView {
    private JsonArray items;
    private Context context;
    private Drug drug;

    public DrugSettingView(final Context context, Drug drug) {
        this.context = context;
        this.drug = drug;
        super.init(context, null, R.layout.activity_drug_setting);

        Toolbar toolbar = get(R.id.toolbar);
        toolbar.setTitle("药品详情");
    }

    private String getOtcName(String otc){
        String name = "其他";
        if (otc == null) {
            name = "其他";
        }
        else if ("RX".equalsIgnoreCase(otc)){
            name = "处方";
        }
        else if ("OTC".equalsIgnoreCase(otc)){
            name = "非处方";
        }
        else{
            name = "其他";
        }
        return name;
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
        String json = AndroidUtil.getFromAssets(context, "drug_setting.json");
        items = (new JsonParser()).parse(json).getAsJsonArray();

        setItemDesc("name", drug.getName());
        setItemDesc("company", drug.getCompany());
        setItemDesc("otc", getOtcName(drug.getOtc()));
        setItemDesc("category", drug.getCategory());
        setItemDesc("form", drug.getForm());
        setItemDesc("meal", drug.getMeal());
        setItemDesc("dosage", drug.getStock() + drug.getDosage());

        return new DefaultItemRecyclerAdapter(context, items, listener);
    }

    private void setItemDesc(String code, String desc){
        for (JsonElement element : items){
            if (element.getAsJsonObject().get("code") == null ||
                element.getAsJsonObject().get("code").isJsonNull()){
                continue;
            }

            String elementCode = element.getAsJsonObject().get("code").getAsString();
            if ("otc".equalsIgnoreCase(elementCode) && !desc.equalsIgnoreCase("其它")){
                element.getAsJsonObject().addProperty("type", "TEXT");
            }

            if (code.equalsIgnoreCase(elementCode)){
                element.getAsJsonObject().addProperty("desc", desc);
                break;
            }
        }
    }

    private AbstractView.OnSelectListener listener = new AbstractView.OnSelectListener(){
        @Override
        public void onSelected(String... arg) {
            String code = arg[0];
            String val = arg[1];

            Log.e("Drug", code + " - " + val);
        }
    };

    @Override
    public RecyclerView getRecyclerView() {
        return get(R.id.drug_setting_recycler_view);
    }

    public Toolbar getToolbar(){
        return get(R.id.toolbar);
    }

    public void setToolbarOnBackPressed(){
        Toolbar toolbar = get(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)context).onBackPressed();
            }
        });
    }
}
