package com.desmond.demo.plan.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.base.adapter.DefaultItemRecyclerAdapter;
import com.desmond.demo.base.view.AbstractRecyclerView;
import com.desmond.demo.base.view.IView;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.model.TimeAndDosage;
import com.desmond.demo.common.util.AndroidUtil;
import com.desmond.demo.common.util.MaterialDialogUtil;
import com.desmond.demo.plan.model.DrugPlan;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DayPlanView extends AbstractRecyclerView {
    private JsonArray items;
    private Context context;
    private Drug drug;
    private DefaultItemRecyclerAdapter adapter;
    private Map<Integer, String[]> timeMap;
    private String times;

    public DayPlanView(final Context context, Drug drug, String times) {
        this.context = context;
        this.drug = drug;
        this.times = times;

        timeMap = new HashMap<Integer, String[]>();
        timeMap.put(1,new String[]{"08:00"});
        timeMap.put(2,new String[]{"08:00", "16:00"});
        timeMap.put(3,new String[]{"08:00", "12:00", "16:00"});
        timeMap.put(4,new String[]{"08:00", "12:00", "16:00", "20:00"});
        timeMap.put(5,new String[]{"07:00", "11:00", "15:00", "18:00", "21:00"});
        timeMap.put(6,new String[]{"07:00", "10:00", "13:00", "16:00", "19:00", "21:00"});
        timeMap.put(7,new String[]{"07:00", "10:00", "12:00", "14:00", "16:00", "18:00", "21:00"});

        super.init(context, null, R.layout.activity_drug_day_plan);
        Toolbar toolbar = get(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_day_plan_title);
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
        String json = AndroidUtil.getFromAssets(context, "drug_day_plan.json");
        items = (new JsonParser()).parse(json).getAsJsonArray();
        setItemDesc("drug", drug.getName());
        setItemDesc("unit", drug.getDosage());

        JsonArray jsonArray = (new JsonParser()).parse(times).getAsJsonArray();
        setItemDesc("times", "每日" + jsonArray.size() + "次");
        addItems(jsonArray);

        adapter = new DefaultItemRecyclerAdapter(context, items, listener);
        return adapter;
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

    private OnSelectListener listener = new OnSelectListener() {
        @Override
        public void onSelected(IView view, String code, final String... arg) {
            if ("times".equalsIgnoreCase(code)){
                String val = arg[0].charAt(2) + "";
                int t = Integer.parseInt(val);
                addItems(t);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void addItems(JsonArray jsonArray){
//        JsonArray jsonArray = (new JsonParser()).parse(dosages).getAsJsonArray();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < jsonArray.size(); i++){
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", "TEXT");
            map.put("code", "timeofday" + (i + 1));
            map.put("title", jsonObject.get("time").getAsString());
            map.put("desc", jsonObject.get("dosages").getAsInt() + jsonObject.get("unit").getAsString());
            list.add(map);
        }
        removeAndAddItems(list);

//        int max = items.size();
//        JsonElement element = items.get(max - 1);
//        while (items.size() > 3){
//            items.remove(3);
//        }
//
//        Gson gson = new Gson();
//        items.addAll(gson.toJsonTree(list).getAsJsonArray());
//        items.add(element);
    }

    private void addItems(int t){
        String[] times = timeMap.get(t);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < times.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", "TEXT");
            map.put("code", "timeofday" + (i + 1));
            map.put("title", times[i]);
            map.put("desc", "1" + drug.getDosage());
            list.add(map);
        }
        removeAndAddItems(list);

//        int max = items.size();
//        JsonElement element = items.get(max - 1);
//        while (items.size() > 3){
//            items.remove(3);
//        }
//
//        Gson gson = new Gson();
//        items.addAll(gson.toJsonTree(list).getAsJsonArray());
//        items.add(element);
    }

    private void removeAndAddItems(List<Map<String, String>> list){
        int max = items.size();
        JsonElement element = items.get(max - 1);
        while (items.size() > 3){
            items.remove(3);
        }

        Gson gson = new Gson();
        items.addAll(gson.toJsonTree(list).getAsJsonArray());
        items.add(element);
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
