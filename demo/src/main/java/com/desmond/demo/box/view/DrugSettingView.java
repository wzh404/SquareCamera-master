package com.desmond.demo.box.view;

import android.content.Context;
import android.content.Intent;
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
import com.desmond.demo.base.view.IView;
import com.desmond.demo.box.adapter.DrugRecyclerAdapter;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.util.AndroidUtil;
import com.desmond.demo.common.util.MaterialDialogUtil;
import com.desmond.demo.plan.activity.NewPlanActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

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
        toolbar.setTitle(R.string.toolbar_drug_detail_title);
    }

    private String getOtcName(String otc){
        String[] arrOtcName = {"处方", "非处方", "其他"};
        String[] arrOtcCode = {"RX", "OTC", "OTHER"};

        if (otc == null) {
            return "其他";
        }

        for (int i = 0; i < arrOtcCode.length; i++){
            if (arrOtcCode[i].equalsIgnoreCase(otc)){
                return arrOtcName[i];
            }
        }

        return "其他";
    }

    private String getOtc(String otcName){
        String[] arrOtcName = {"处方", "非处方", "其他"};
        String[] arrOtcCode = {"RX", "OTC", "OTHER"};

        for (int i = 0; i < arrOtcName.length; i++){
            if (arrOtcName[i].equalsIgnoreCase(otcName)){
                return arrOtcCode[i];
            }
        }

        return "OTHER";
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
        String json = AndroidUtil.getFromAssets(context, "drug_setting.json");
        items = (new JsonParser()).parse(json).getAsJsonArray();

        setItemDesc("name", drug.getName());
        setItemDesc("company", drug.getCompany());

        setItemDesc("code", "国药准字" + drug.getCode());
        setOtcDesc(getOtcName(drug.getOtc()));
        setItemDesc("category", drug.getCategory());
        setItemDesc("form", drug.getForm());
        setItemDesc("meal", drug.getMeal());

        setItemDesc("dosage", drug.getStock() + drug.getDosage());
        addItemProperty("dosage", "dosage", drug.getDosage());
        addItemProperty("dosage", "stock", drug.getStock() + "");

        return new DefaultItemRecyclerAdapter(context, items, listener);
    }

    private void addItemProperty(String code, String key, String val){
        for (JsonElement element : items) {
            if (element.getAsJsonObject().get("code") == null ||
                    element.getAsJsonObject().get("code").isJsonNull()) {
                continue;
            }

            String elementCode = element.getAsJsonObject().get("code").getAsString();
            if (code.equalsIgnoreCase(elementCode)){
                element.getAsJsonObject().addProperty(key, val);
                break;
            }
        }
    }

    private void setItemDesc(String code, String desc){
        addItemProperty(code, "desc", desc);
    }

    private void setOtcDesc(String desc){
        for (JsonElement element : items){
            if (element.getAsJsonObject().get("code") == null ||
                element.getAsJsonObject().get("code").isJsonNull()){
                continue;
            }

            String elementCode = element.getAsJsonObject().get("code").getAsString();
            if ("otc".equalsIgnoreCase(elementCode)){
                element.getAsJsonObject().addProperty("desc", desc);
                if (!desc.equalsIgnoreCase("其它")) {
                    element.getAsJsonObject().addProperty("type", "TEXT");
                }

                break;
            }
        }
    }

    private AbstractView.OnSelectListener listener = new AbstractView.OnSelectListener(){
        @Override
        public void onSelected(IView view, final String code, final int selected, final String... arg) {
            final Realm realm = Realm.getDefaultInstance();
            RealmResults<Drug> result = realm.where(Drug.class).equalTo("id", drug.getId()).findAll();
            if (result.size() <= 0) return;

            final Drug realmDrug = result.get(0);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if ("meal".equalsIgnoreCase(code)){
                        String val = arg[0];
                        realmDrug.setMeal(val);
                        drug.setMeal(val);
                    }
                    else if ("dosage".equalsIgnoreCase(code)){
                        realmDrug.setDosage(arg[1]);
                        realmDrug.setStock(Integer.parseInt(arg[0]));

                        addItemProperty("dosage", "stock", arg[0]);
                        addItemProperty("dosage", "dosage", arg[1]);
                        drug.setDosage(arg[1]);
                    }
                    else if ("otc".equalsIgnoreCase(code)){
                        String val = arg[0];
                        realmDrug.setOtc(getOtc(val));
                    }
                    else if ("plan".equalsIgnoreCase(code)){
                        startNewPlan();
                    }
                }
            });
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

    private void startNewPlan(){
        Intent intent = new Intent(context, NewPlanActivity.class);
        intent.putExtra("drug", drug);
        context.startActivity(intent);
    }
}
