package com.desmond.demo.plan.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.base.view.AbstractSwipeRefresh;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.common.rxbus.RxBus;
import com.desmond.demo.common.util.DateUtil;
import com.desmond.demo.plan.adapter.DrugPlanRecyclerAdapter;
import com.desmond.demo.plan.model.DrugPlan;

import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugPlanView extends AbstractSwipeRefresh {
    private List<DrugPlan> items;
    private DrugPlanItemView.ClickListener clickListener;
//    private CallbackDrugPlan  callback;

    public DrugPlanView(Context context, ViewGroup container, List<DrugPlan> items, DrugPlanItemView.ClickListener clickListener) {
//        this.callback = callback;
        this.clickListener = clickListener;
        this.items = items;
        super.init(context, container, R.layout.fragment_drug_plan);

        Toolbar toolbar = get(R.id.toolbar);
        toolbar.setTitle("用药计划");
        toolbar.inflateMenu(R.menu.menu_drug_plan);
    }

    @Override
    public void freshData() {
//        callback.fresh();
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
//        this.items = callback.getItems();
        return new DrugPlanRecyclerAdapter(context, items, clickListener);
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

        /* 日期分组 */
        int groupIndex = getGroupIndex(plan);
        if (groupIndex == -1){
            DrugPlan p = new DrugPlan();
            p.setId(-1L);
            p.setStartDate(plan.getStartDate());
            items.add(0, p);
            groupIndex = 1;
        }

        items.add(groupIndex + 1, plan);
        notifyReminder();
        getAdapter().notifyDataSetChanged();
    }

    public void deleteItem(DrugPlan plan){
        int groupIndex = getGroupIndex(plan);
        int itemsCount = getItemsByGroup(plan);
        if (itemsCount == 1){
            items.remove(groupIndex);
        }
        items.remove(plan);
        notifyReminder();
        getAdapter().notifyDataSetChanged();
    }

    private int getGroupIndex(DrugPlan plan){
        for (int i = 0; i < items.size(); i++){
            int days = DateUtil.diffDay(plan.getStartDate(), items.get(i).getStartDate());
            if (days == 0 && items.get(i).getId().longValue() == -1L){
                return i;
            }
        }

        return -1;
    }

    private int getItemsByGroup(DrugPlan plan){
        int cnt = 0;
        for (int i = 0; i < items.size(); i++){
            int days = DateUtil.diffDay(plan.getStartDate(), items.get(i).getStartDate());
            if (days == 0 && items.get(i).getId().longValue() != -1L){
                cnt++;
            }
        }

        return cnt;
    }

    /**
     * 通知提醒更新
     */
    private void notifyReminder(){
        Result r = new Result();
        r.setTag("reminder");
        r.setCode("OK");

        RxBus.get().post("reminder", r);
    }
}
