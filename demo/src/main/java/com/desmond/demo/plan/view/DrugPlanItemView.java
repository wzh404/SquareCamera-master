package com.desmond.demo.plan.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.base.view.AbstractSwipeRefresh;
import com.desmond.demo.base.view.AbstractView;
import com.desmond.demo.plan.adapter.DrugPlanRecyclerAdapter;
import com.desmond.demo.plan.model.DrugPlan;

import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugPlanItemView extends AbstractView {
    public DrugPlanItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_drug_plan);
    }
}
