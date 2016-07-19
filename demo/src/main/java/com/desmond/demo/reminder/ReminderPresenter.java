package com.desmond.demo.reminder;

import com.desmond.demo.base.presenter.DefaultPresenter;
import com.desmond.demo.plan.model.DrugPlan;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by wangzunhui on 2016/7/19.
 */
public class ReminderPresenter extends DefaultPresenter {

    public ReminderPresenter(){
        super();
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
