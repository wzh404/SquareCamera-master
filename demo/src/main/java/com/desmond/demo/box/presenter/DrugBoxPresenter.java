package com.desmond.demo.box.presenter;

import com.desmond.demo.common.action.Result;
import com.desmond.demo.box.model.DrugBox;
import com.desmond.demo.box.view.DrugBoxView;
import com.desmond.demo.common.DefaultPresenter;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugBoxPresenter extends DefaultPresenter implements DrugBoxView.CallbackDrugBoxView{
    public DrugBoxPresenter(Action1<Result> action1){
        super();
        register("drug", action1);;
    }

    @Override
    public void fresh() {

    }

    @Override
    public List<DrugBox> getItems() {
        List<DrugBox> items = new ArrayList<>();
        return items;
    }

    public void drug(String code) {
        Observable<JsonObject> observable = getWebService().drug(code);
        call(observable, "drug");
    }
}
