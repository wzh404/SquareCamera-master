package com.desmond.demo.plan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.BR;
import com.desmond.demo.R;
import com.desmond.demo.base.view.EmptyView;
import com.desmond.demo.box.view.DrugItemView;
import com.desmond.demo.base.view.RecyclerViewHolder;
import com.desmond.demo.plan.model.DrugPlan;
import com.desmond.demo.plan.view.DrugPlanItemView;

import java.util.List;

/**
 * Created by WIN10 on 2016/2/1.
 */
public class DrugPlanRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    final int defaultType = 1;
    final int emptyType = 0;

    private List<DrugPlan> items;
    private Context context;

    public DrugPlanRecyclerAdapter(Context context, List<DrugPlan> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == emptyType){
            EmptyView v = new EmptyView(context, parent);
            v.setHintText(R.mipmap.empty_plan, R.string.empty_plan);
            return new RecyclerViewHolder(v);
        }

        return new RecyclerViewHolder(new DrugPlanItemView(context, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (holder.getItemViewType() == emptyType)
            return;

        holder.getBinding().setVariable(BR.plan, items.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getId() == 0)
            return emptyType;

        return defaultType;
    }
}