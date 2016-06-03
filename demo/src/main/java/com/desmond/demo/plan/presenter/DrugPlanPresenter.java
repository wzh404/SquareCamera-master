package com.desmond.demo.plan.presenter;

import com.desmond.demo.base.presenter.DefaultPresenter;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.plan.model.DrugPlan;
import com.desmond.demo.plan.view.DrugPlanView;

import java.util.ArrayList;
import java.util.List;

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

        DrugPlan plan = new DrugPlan();
        plan.setId(0);
        items.add(plan);

        return items;
    }

//    public void drug(String code) {
//        Observable<JsonObject> observable = getWebService().drug(code);
//        call(observable, "drug");
//    }
}
