package com.desmond.demo.plan.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.base.view.AbstractSwipeRefresh;
import com.desmond.demo.plan.adapter.DrugPlanRecyclerAdapter;
import com.desmond.demo.plan.model.DrugPlan;

import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugPlanView extends AbstractSwipeRefresh {
    private List<DrugPlan> items;
    private CallbackDrugPlan  callback;

    public DrugPlanView(Context context, ViewGroup container, CallbackDrugPlan callback) {
        this.callback = callback;
        super.init(context, container, R.layout.fragment_drug_plan);

        Toolbar toolbar = get(R.id.toolbar);
        toolbar.setTitle("用药");
        toolbar.inflateMenu(R.menu.menu_drug_plan);
    }

    @Override
    public void freshData() {
        callback.fresh();
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
        this.items = callback.getItems();
        return new DrugPlanRecyclerAdapter(context, items);
    }

    @Override
    public SwipeRefreshLayout getSwipeRefresh() {
        return get(R.id.drug_plan_swipe_refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return get(R.id.drug_plan_recycler_view);
    }

    public void addItem(DrugPlan plan){
        if (items.size() == 1 && items.get(0).getId().intValue() == 0){
            items.clear();
        }

        items.add(plan);
        getAdapter().notifyDataSetChanged();
    }

    public interface CallbackDrugPlan{
        public void fresh();
        public List<DrugPlan> getItems();
    }
}
