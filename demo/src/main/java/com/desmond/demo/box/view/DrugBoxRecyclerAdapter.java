package com.desmond.demo.box.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.desmond.demo.BR;
import com.desmond.demo.common.IView;
import com.desmond.demo.common.RecyclerViewHolder;

import java.util.List;

/**
 * Created by WIN10 on 2016/2/1.
 */
public class DrugBoxRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    final int defaultType = 1;

    private List<Object> items;
    private Context context;

    public DrugBoxRecyclerAdapter(Context context, List<Object> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IView v = new DrugBoxView();
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (holder.getItemViewType() == 0)
            return;

        holder.getBinding().setVariable(BR.box, items.get(position));
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
