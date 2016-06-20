package com.desmond.demo.box.presenter;

import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.box.view.DrugView;
import com.desmond.demo.base.presenter.DefaultPresenter;
import com.desmond.demo.common.util.Constants;
import com.desmond.demo.plan.model.DrugPlan;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugPresenter extends DefaultPresenter {
    public DrugPresenter(Action1<Result> action1){
        super();
        register("drug", action1);;
    }


    public void drug(String code) {
        Observable<JsonObject> observable = getWebService().drug(code);
        call(observable, "drug");
    }


    public void deleteDrug(final Drug drug){
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Drug> result = realm.where(Drug.class).equalTo("id", drug.getId()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (result.size() > 0) result.deleteFromRealm(0);
            }
        });
    }

    public RealmResults<Drug> queryDrugAsync(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Drug> result = realm.where(Drug.class)
                .equalTo("state", Constants.DRUG_STATE_NORMAL)
                .findAllSortedAsync("time", Sort.DESCENDING);
        return result;
    }

    public boolean allowDelete(Drug drug){
        Realm realm = Realm.getDefaultInstance();
        Long count = realm.where(DrugPlan.class)
                .equalTo("drug.id", drug.getId())
                .equalTo("state", "N")
                .count();

        return count <= 0;
    }
}
