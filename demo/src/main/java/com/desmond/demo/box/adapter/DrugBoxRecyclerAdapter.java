package com.desmond.demo.box.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;


import com.desmond.demo.BR;
import com.desmond.demo.R;
import com.desmond.demo.box.model.DrugBox;
import com.desmond.demo.box.view.DrugBoxItemView;
import com.desmond.demo.common.IView;
import com.desmond.demo.common.RecyclerViewHolder;

import java.util.List;

/**
 * Created by WIN10 on 2016/2/1.
 */
public class DrugBoxRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    final int defaultType = 1;

    private List<DrugBox> items;
    private Context context;

    public DrugBoxRecyclerAdapter(Context context, List<DrugBox> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IView v = new DrugBoxItemView(context, parent);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (holder.getItemViewType() == 0)
            return;

        ((DrugBoxItemView)holder.getIView()).setDrugOtc(items.get(position).getOtc());
        holder.getBinding().setVariable(BR.drug, items.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return defaultType;
    }
}
