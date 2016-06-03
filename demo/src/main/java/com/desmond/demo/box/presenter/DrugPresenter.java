package com.desmond.demo.box.presenter;

import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.box.view.DrugView;
import com.desmond.demo.base.presenter.DefaultPresenter;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugPresenter extends DefaultPresenter implements DrugView.CallbackDrugView{
    public DrugPresenter(Action1<Result> action1){
        super();
        register("drug", action1);;
    }

    @Override
    public void fresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public List<Drug> getItems() {
        List<Drug> items = new ArrayList<>();

        Drug drug = new Drug();
        drug.setId(0);
        items.add(drug);

        return items;
    }

    public void drug(String code) {
        Observable<JsonObject> observable = getWebService().drug(code);
        call(observable, "drug");
    }
}
