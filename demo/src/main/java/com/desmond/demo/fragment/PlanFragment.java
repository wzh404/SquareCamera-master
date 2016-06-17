package com.desmond.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.desmond.demo.common.action.Result;
import com.desmond.demo.plan.model.DrugPlan;
import com.desmond.demo.plan.presenter.DrugPlanPresenter;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DrugPlanPresenter presenter = new DrugPlanPresenter(drugPlanAction1);
        DrugPlanView view = new DrugPlanView(getContext(), container, presenter);


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

            } else {
                Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
