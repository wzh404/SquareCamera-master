package com.desmond.demo.plan.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.base.adapter.DefaultItemRecyclerAdapter;
import com.desmond.demo.base.view.AbstractRecyclerView;
import com.desmond.demo.base.view.IView;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.model.TimeAndDosage;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.common.rxbus.RxBus;
import com.desmond.demo.common.util.AndroidUtil;
import com.desmond.demo.common.util.DateUtil;
import com.desmond.demo.common.util.MaterialDialogUtil;
import com.desmond.demo.plan.activity.NewPlanActivity;
import com.desmond.demo.plan.model.DrugPlan;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class NewPlanView extends AbstractRecyclerView {
    private JsonArray items;
    private Context context;
    private Drug drug;
    private DrugPlan plan;
    private DefaultItemRecyclerAdapter adapter;

    public NewPlanView(final Context context, Drug drug, DrugPlan plan) {
        this.context = context;
        this.drug = drug;
        this.plan = plan;

        super.init(context, null, R.layout.activity_new_drug_plan);
        Toolbar toolbar = get(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_new_plan_title);
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
        String json = AndroidUtil.getFromAssets(context, "drug_plan.json");
        items = (new JsonParser()).parse(json).getAsJsonArray();

        setItemDesc("drug", drug.getName());
        setItemDesc("user", "本人");

        setItemDesc("interval", plan.getIntervalDesc());
        setItemDesc("time", plan.getDosageDesc());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        setItemDesc("date", sdf.format(new Date()));
        setItemDesc("days", plan.getDaysDesc());

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
        public void onSelected(IView view, String code, int selected, final String... arg) {
            if ("interval".equalsIgnoreCase(code)) {
                String val = arg[0];
                if ("每日".equalsIgnoreCase(val)) {
                    plan.setInterval("everyday");
                    plan.setDefaultDosageOfDay(drug.getDosage());
                    setItemDesc("time", plan.getDosageDesc());
                    adapter.notifyDataSetChanged();
                }
                else if ("每周".equalsIgnoreCase(val)) {
                    plan.setInterval("week");
                    showWeek(view);
                }
                else if ("间隔天".equalsIgnoreCase(val)) {
                    plan.setInterval("days");
                    showIntervalDays(view);
                }
                else if ("间隔小时".equalsIgnoreCase(val)) {
                    plan.setInterval("hours");
                    showIntervalHours(view);
                }
                else if ("一次性".equalsIgnoreCase(val)) {
                    plan.setInterval("temp");
                    plan.setDefaultDosageOfTemp(drug.getDosage());
                    setItemDesc("time", plan.getDosageDesc());
                    adapter.notifyDataSetChanged();
                }
            }
            else if ("time".equalsIgnoreCase(code)){
                if ("everyday".equalsIgnoreCase(plan.getInterval()) ||
                        "week".equalsIgnoreCase(plan.getInterval()) ||
                        "days".equalsIgnoreCase(plan.getInterval())) {
                    ((NewPlanActivity) context).startDayPlan(plan.getDosages());
                }
                else if ("temp".equalsIgnoreCase(plan.getInterval())){
                    showPlanTemp();
                }
                else if ("hours".equalsIgnoreCase(plan.getInterval())){
                    MaterialDialogUtil.showDosage(context, 2, drug.getDosage(), false, new MaterialDialogUtil.DosageCallback() {
                        @Override
                        public void onClick(final int dosage, final String unit) {
                            plan.setDosageOfTemp(DateUtil.getCurrentTime(), dosage, drug.getDosage());
                            setItemDesc("time", plan.getDosageDesc());
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            else if ("date".equalsIgnoreCase(code)){
                MaterialDialogUtil.DateCallback callback = new MaterialDialogUtil.DateCallback() {
                    @Override
                    public void onClick(Date date) {
                        plan.setStartDate(date);
                        plan.setCloseDate(DateUtil.addDate(plan.getStartDate(), plan.getDays()));

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        setItemDesc("date", sdf.format(date));
                        adapter.notifyDataSetChanged();
                    }
                };
                MaterialDialogUtil.showDate(context, callback);
            }
            else if ("days".equalsIgnoreCase(code)){
                switch (selected){
                    case 0:
                        plan.setDays(-1);  // 剩余剂量服用时间
                        plan.setCloseDate(DateUtil.addDate(plan.getStartDate(), 0));
                        break;

                    case 1:
                        plan.setDays(-2); // 持续服用
                        plan.setCloseDate(DateUtil.addDate(plan.getStartDate(), 365));
                        break;

                    case 2:
                        plan.setDays(3);  // 3天
                        plan.setCloseDate(DateUtil.addDate(plan.getStartDate(), 3));
                        break;

                    case 3:
                        plan.setDays(7);  // 一周
                        plan.setCloseDate(DateUtil.addDate(plan.getStartDate(), 7));
                        break;

                    case 4:
                        plan.setDays(30); // 一月
                        plan.setCloseDate(DateUtil.addDate(plan.getStartDate(), 30));
                        break;

                    case 5:
                        MaterialDialogUtil.InputCallback callback = new MaterialDialogUtil.InputCallback() {
                            @Override
                            public void onClick(String value) {
                                plan.setDays(Integer.parseInt(value));
                                plan.setCloseDate(DateUtil.addDate(plan.getStartDate(), plan.getDays()));
                                setItemDesc("days", value + "天");
                                adapter.notifyDataSetChanged();
                            }
                        };
                        MaterialDialogUtil.showInputDialog(context, callback);
                        break;
                }
            }
            else if ("plan".equalsIgnoreCase(code)){ // 确认计划
                Realm realm = Realm.getDefaultInstance();
                Long count = realm.where(DrugPlan.class)
                        .equalTo("drug.id", drug.getId())
                        .greaterThanOrEqualTo("closeDate", plan.getStartDate())
                        .equalTo("state", "N")
                        .count();

                if (count > 0 ){
                    String content = String.format("%s至%s已经存在了另外一个用药计划。", DateUtil.getDate(plan.getStartDate()), DateUtil.getDate(plan.getCloseDate()));
                    MaterialDialogUtil.showMessage(context, drug.getName(), content);
                    return;
                }

                RealmResults<Drug> result = realm.where(Drug.class).equalTo("id", drug.getId()).findAll();
                if (result.size() <= 0) return;

                final Drug realmDrug = result.get(0);
                plan.setDrug(realmDrug);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(plan);
                    }
                });

                Result r = new Result();
                r.setTag("plan");
                r.setCode("OK");
                r.setMsg(plan.getId().toString());

                RxBus.get().post("plan", r);
                ((NewPlanActivity)context).finish();
            }
        }
    };


    private void showPlanTemp(){
        MaterialDialogUtil.TimeAndDosageCallback callback = new MaterialDialogUtil.TimeAndDosageCallback(){
            @Override
            public void onClick(String time, int dosage) {
                plan.setDosageOfTemp( time, dosage, drug.getDosage());
                setItemDesc("time", plan.getDosageDesc());
                adapter.notifyDataSetChanged();
            }
        };
        MaterialDialogUtil.showPlanTimeAndDosage(context, "12:00", 2, drug.getDosage(), callback);
    }


    private void showIntervalHours(final IView view) {
        MaterialDialog.ListCallback callback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View v, int which, CharSequence text) {
                setItemDesc("interval", text.toString());
                plan.setIntervalDetails((which + 1) + "");

                plan.setDefaultDosageOfHours(drug.getDosage());
                setItemDesc("time", plan.getDosageDesc());
                adapter.notifyDataSetChanged();
            }
        };

        String[] items = new String[12];
        for (int i = 0; i < items.length; i++){
            items[i] = "间隔" + (i + 1) +"小时";
        }

        MaterialDialogUtil.showList(view.getView(), null,items, callback);
    }

    private void showIntervalDays(final IView view) {
        MaterialDialog.ListCallback callback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View v, int which, CharSequence text) {
                setItemDesc("interval", text.toString());
                plan.setIntervalDetails((which + 1) + "");

                plan.setDefaultDosageOfDay(drug.getDosage());
                setItemDesc("time", plan.getDosageDesc());
                adapter.notifyDataSetChanged();
            }
        };
        String[] items = new String[6];
        for (int i = 0; i < items.length; i++){
            items[i] = "间隔" + (i + 1) +"天";
        }
        MaterialDialogUtil.showList(view.getView(), null, items, callback);
    }

    private void showWeek(final IView view){
        final String[] weeks ={"一", "二", "三", "四", "五", "六", "日"};
        MaterialDialog.SingleButtonCallback callback = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                String text = "";
                for (int k : dialog.getSelectedIndices())
                    text += weeks[k];

                plan.setIntervalDetails(text);
                plan.setDefaultDosageOfDay(drug.getDosage());
                setItemDesc("time", plan.getDosageDesc());
                setItemDesc("interval", plan.getIntervalDesc());
                adapter.notifyDataSetChanged();
            }
        };

        String[] items = new String[weeks.length];
        for (int i = 0; i < items.length; i++){
            items[i] = "星期" + weeks[i];
        }

        MaterialDialogUtil.showMultiChoiceLimited(view.getView(), items, callback);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return get(R.id.new_plan_recycler_view);
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
