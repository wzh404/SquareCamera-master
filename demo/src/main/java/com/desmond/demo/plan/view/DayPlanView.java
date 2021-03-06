package com.desmond.demo.plan.view;

import android.content.Context;
import android.content.Intent;
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
import com.desmond.demo.plan.activity.DayPlanActivity;
import com.desmond.demo.plan.activity.NewPlanActivity;
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
    private TimeAndDosage[] timeAndDosages;

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

    public String getDosages(){
        Gson gson = new Gson();
        return gson.toJson(timeAndDosages);
    }

    private OnSelectListener listener = new OnSelectListener() {
        @Override
        public void onSelected(IView view, String code, int selected, final String... arg) {
            if ("times".equalsIgnoreCase(code)){
                addItems(selected + 1);
                adapter.notifyDataSetChanged();
            }
            else if (code.startsWith("timeofday")){
                String[] t = code.split("_");
                final int inx = Integer.parseInt(t[1]);

                final JsonElement element = items.get(3 + inx - 1);
                String time = element.getAsJsonObject().get("title").getAsString();
                int dosage = Integer.parseInt(element.getAsJsonObject().get("dosages").getAsString());

                MaterialDialogUtil.TimeAndDosageCallback callback = new MaterialDialogUtil.TimeAndDosageCallback(){
                    @Override
                    public void onClick(String time, int dosage) {
                        element.getAsJsonObject().addProperty("title", time);
                        element.getAsJsonObject().addProperty("dosages", dosage);
                        element.getAsJsonObject().addProperty("desc", dosage + drug.getDosage());

                        timeAndDosages[inx - 1].setTime(time);
                        timeAndDosages[inx - 1].setDosages(dosage);

                        adapter.notifyDataSetChanged();
                    }
                };
                MaterialDialogUtil.showPlanTimeAndDosage(context, time, dosage, drug.getDosage(), callback);
            }
            else if (code.equalsIgnoreCase("plan")){
                DayPlanActivity activity = (DayPlanActivity)context;
                Intent data = new Intent(context, NewPlanActivity.class);
                data.putExtra("dosages", getDosages());
//                Log.e("Drug", "Destroy: " + getDosages());
                activity.setResult(9, data);

                activity.finish();
            }
        }
    };

    private void addItems(JsonArray jsonArray){
        timeAndDosages = new TimeAndDosage[jsonArray.size()];
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < jsonArray.size(); i++){
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String time = jsonObject.get("time").getAsString();
            int dosage = jsonObject.get("dosages").getAsInt();
            Map<String, String> map = createItem(i + 1, time, dosage);

            timeAndDosages[i] = new TimeAndDosage(time, dosage, drug.getDosage());

            list.add(map);
        }
        removeAndAddItems(list);
    }

    private Map<String, String> createItem(int inx, String time, int dosage){
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", "DEFAULT");
        map.put("code", "timeofday_" + inx);
        map.put("title", time);
        map.put("dosages", dosage + "");
        map.put("unit", drug.getDosage());
        map.put("select", "select");
        map.put("desc", dosage + drug.getDosage());

        return map;
    }

    private void addItems(int t){
        String[] times = timeMap.get(t);
        timeAndDosages = new TimeAndDosage[times.length];
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < times.length; i++) {
            Map<String, String> map = createItem(i + 1, times[i], 1);

            timeAndDosages[i] = new TimeAndDosage(times[i], 1, drug.getDosage());
            list.add(map);
        }
        removeAndAddItems(list);
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
