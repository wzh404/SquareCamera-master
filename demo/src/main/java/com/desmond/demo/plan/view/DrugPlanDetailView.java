package com.desmond.demo.plan.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.desmond.demo.R;
import com.desmond.demo.base.adapter.DefaultItemRecyclerAdapter;
import com.desmond.demo.base.view.AbstractRecyclerView;
import com.desmond.demo.base.view.IView;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.model.TimeAndDosage;
import com.desmond.demo.common.util.AndroidUtil;
import com.desmond.demo.common.util.DateUtil;
import com.desmond.demo.common.util.MaterialDialogUtil;
import com.desmond.demo.plan.activity.DayPlanActivity;
import com.desmond.demo.plan.activity.NewPlanActivity;
import com.desmond.demo.plan.model.DrugPlan;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugPlanDetailView extends AbstractRecyclerView {
    private JsonArray items;
    private Context context;
    private DrugPlan plan;
    private DefaultItemRecyclerAdapter adapter;


    public DrugPlanDetailView(final Context context, DrugPlan plan) {
        this.context = context;
        this.plan = plan;

        super.init(context, null, R.layout.activity_drug_day_plan);
        Toolbar toolbar = get(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_plan_detail_title);
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
        String json = AndroidUtil.getFromAssets(context, "drug_plan_detail.json");
        items = (new JsonParser()).parse(json).getAsJsonArray();

        setItemDesc("drug", plan.getDrug().getName());
        setItemDesc("user", "self".equalsIgnoreCase(plan.getUser()) ? "本人" : "其它");
        setItemDesc("startDate", DateUtil.getDate(plan.getStartDate()));
        setItemDesc("closeDate", DateUtil.getDate(plan.getCloseDate()));
        setItemDesc("interval", plan.getIntervalDesc());

        JsonElement element = (new JsonParser()).parse(plan.getDosages());
        if (element.isJsonArray()){
            JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject jsonObject = array.get(i).getAsJsonObject();
                String time = jsonObject.get("time").getAsString();
                int dosage = jsonObject.get("dosages").getAsInt();
                String unit = jsonObject.get("unit").getAsString();

                items.add(createItem(time, dosage + unit));
            }
        }
        else if ("temp".equalsIgnoreCase(plan.getInterval())){
            JsonObject jsonObject = element.getAsJsonObject();
            String time = jsonObject.get("time").getAsString();
            int dosage = jsonObject.get("dosages").getAsInt();
            String unit = jsonObject.get("unit").getAsString();

            items.add(createItem(time, dosage + unit));
        }
        else if ("hours".equalsIgnoreCase(plan.getInterval())){
            JsonObject jsonObject = element.getAsJsonObject();
            int dosage = jsonObject.get("dosages").getAsInt();
            String unit = jsonObject.get("unit").getAsString();

            items.add(createItem("用药剂量", dosage + unit));
        }

        adapter = new DefaultItemRecyclerAdapter(context, items, null);
        return adapter;
    }

    private JsonElement createItem(String title, String desc){
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", "TEXT");
        map.put("code", "timeofday_");
        map.put("title", title);
        map.put("desc", desc);

        Gson gson = new Gson();
        return gson.toJsonTree(map);
    }

    private void addItemProperty(String code, String key, String val) {
        for (JsonElement element : items) {
            if (element.getAsJsonObject().get("code") == null ||
                    element.getAsJsonObject().get("code").isJsonNull()) {
                continue;
            }

            String elementCode = element.getAsJsonObject().get("code").getAsString();
            if (code.equalsIgnoreCase(elementCode)) {
                element.getAsJsonObject().addProperty(key, val);
                break;
            }
        }
    }

    private void setItemDesc(String code, String desc) {
        addItemProperty(code, "desc", desc);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return get(R.id.day_plan_recycler_view);
    }

    public Toolbar getToolbar() {
        return get(R.id.toolbar);
    }

    public void setToolbarOnBackPressed() {
        Toolbar toolbar = get(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) context).onBackPressed();
            }
        });
    }
}
