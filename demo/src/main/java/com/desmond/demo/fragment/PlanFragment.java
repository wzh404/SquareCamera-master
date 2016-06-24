package com.desmond.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.desmond.demo.base.view.IView;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.plan.activity.DrugPlanDetailActivity;
import com.desmond.demo.plan.model.DrugPlan;
import com.desmond.demo.plan.presenter.DrugPlanPresenter;
import com.desmond.demo.plan.view.DrugPlanItemView;
import com.desmond.demo.plan.view.DrugPlanView;
import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * 用药
 *
 * Created by wangzunhui on 2016/6/2.
 */
public class PlanFragment extends Fragment {
    private DrugPlanPresenter presenter;
    private DrugPlanView view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        presenter = new DrugPlanPresenter(drugPlanAction1);
        view = new DrugPlanView(getContext(), container, presenter.getItems(), clickListener);

        return view.getView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Action1 drugPlanAction1 = new Action1<Result>() {
        @Override
        public void call(Result result) {
            if (result.isResult("plan", "OK")) {
                Log.e(PlanFragment.class.getSimpleName(), "---------" + result.getMsg());
                Long id = Long.parseLong(result.getMsg());

                Realm realm = Realm.getDefaultInstance();
                RealmResults<DrugPlan> realmResults = realm.where(DrugPlan.class).equalTo("id", id).findAll();
                if (realmResults.size() <= 0) return;
                DrugPlan plan = realmResults.get(0);
                view.addItem(plan);
            } else {
                Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        presenter.destroy();
    }

    private DrugPlanItemView.ClickListener clickListener = new DrugPlanItemView.ClickListener(){
        @Override
        public void onClick(View v, DrugPlan plan) {
            startPlanDetail(plan);
        }

        @Override
        public void onLongClick(View v, int which, DrugPlan plan) {
            switch(which){
                case 2 : // delete
                    view.deleteItem(plan);
                    presenter.deleteDrug(plan);
                    break;
            }
        }
    };

    private void startPlanDetail(DrugPlan plan){
        Intent intent = new Intent(getContext(), DrugPlanDetailActivity.class);
        intent.putExtra("plan", plan);
        startActivity(intent);
    }
}
