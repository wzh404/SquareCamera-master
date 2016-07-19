package com.desmond.demo.plan.presenter;

import android.util.Log;

import com.desmond.demo.base.presenter.DefaultPresenter;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.common.util.Constants;
import com.desmond.demo.plan.model.DrugPlan;
import com.desmond.demo.plan.view.DrugPlanView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.functions.Action1;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugPlanPresenter extends DefaultPresenter {
    public DrugPlanPresenter(Action1<Result> action1){
        super();
        register("plan", action1);
    }

    public List<DrugPlan> getItems() {
        List<DrugPlan> items = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DrugPlan> result = realm.where(DrugPlan.class).findAll();

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

    public void deleteDrug(final DrugPlan plan){
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<DrugPlan> result = realm.where(DrugPlan.class).equalTo("id", plan.getId()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (result.size() > 0) result.deleteFromRealm(0);
            }
        });
    }

    public void cancelPlan(final DrugPlan plan){
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<DrugPlan> result = realm.where(DrugPlan.class).equalTo("id", plan.getId()).findAll();
        if (result.size() <= 0) return;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.get(0).setState("C");
            }
        });
    }

    public RealmResults<DrugPlan> queryPlanAsync(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DrugPlan> result = realm.where(DrugPlan.class)
                .lessThanOrEqualTo("startDate", new Date())
                .greaterThanOrEqualTo("closeDate", new Date())
                .findAllAsync();
        return result;
    }
}
