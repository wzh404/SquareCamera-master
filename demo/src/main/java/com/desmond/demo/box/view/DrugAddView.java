package com.desmond.demo.box.view;

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
import com.desmond.demo.box.activity.DrugAddActivity;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.util.AndroidUtil;
import com.desmond.demo.common.util.MaterialDialogUtil;
import com.desmond.demo.plan.activity.NewPlanActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugAddView extends AbstractRecyclerView {
    private JsonArray items;
    private Context context;

    public DrugAddView(final Context context) {
        this.context = context;
        super.init(context, null, R.layout.activity_add_drug);
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
        String json = AndroidUtil.getFromAssets(context, "drug_add.json");
        items = (new JsonParser()).parse(json).getAsJsonArray();

//        setItemDesc("name", drug.getName());


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

    private OnSelectListener listener = new OnSelectListener(){
        @Override
        public void onSelected(IView view, final String code, final int selected, final String... arg) {
            Log.e("Drug", "----------------" + code);

            if ("name".equalsIgnoreCase(code)){
                MaterialDialogUtil.showInputDrugCode(view.getView().getContext(), new MaterialDialogUtil.InputCallback() {
                    @Override
                    public void onClick(String value) {
                        if (context instanceof DrugAddActivity){
                            ((DrugAddActivity)context).queryDrugAndFinish(value);
                        }
                    }
                });
            }
            else if ("scan".equalsIgnoreCase(code)){
                if (context instanceof DrugAddActivity){
                    ((DrugAddActivity)context).scan();
                }
            }
            else if ("more".equalsIgnoreCase(code)){

            }
            else if (code.length() == 9){
                if (context instanceof DrugAddActivity){
                    ((DrugAddActivity)context).queryDrugAndFinish(code);
                }
            }
        }
    };

    @Override
    public RecyclerView getRecyclerView() {
        return get(R.id.drug_add_recycler_view);
    }

    public Toolbar getToolbar(){
        return get(R.id.toolbar);
    }
}
