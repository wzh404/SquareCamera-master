package com.desmond.demo.plan.presenter;

import android.util.Log;

import com.desmond.demo.base.presenter.DefaultPresenter;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.plan.model.DrugPlan;
import com.desmond.demo.plan.view.DrugPlanView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugPlanPresenter extends DefaultPresenter implements DrugPlanView.CallbackDrugPlan{
    public DrugPlanPresenter(Action1<Result> action1){
        super();
//        register("plan", action1);
    }

    @Override
    public void fresh() {
    }

    @Override
    public List<DrugPlan> getItems() {
        List<DrugPlan> items = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DrugPlan> result = realm.where(DrugPlan.class).findAll();

//        for (int i = 0; i < result.size(); i++) {
//            DrugPlan plan = result.get(i);
//            Log.e("Drug", "result2 is " + plan.getDosages() + " - " + plan.getCloseDate() + " - " + plan.getStartDate());
//        }
        if (result.size() <= 0) {
            DrugPlan plan = new DrugPlan();
            plan.setId(0L);
            items.add(plan);
        }
        else{
            for (DrugPlan plan : result){
                items.add(plan);
            }
        }

        return items;
    }

//    public void drug(String code) {
//        Observable<JsonObject> observable = getWebService().drug(code);
//        call(observable, "drug");
//    }
}
