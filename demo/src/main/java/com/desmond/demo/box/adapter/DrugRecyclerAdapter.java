package com.desmond.demo.box.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.BR;
import com.desmond.demo.base.EmptyView;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.model.DrugBox;
import com.desmond.demo.box.view.DrugItemView;
import com.desmond.demo.common.IView;
import com.desmond.demo.common.RecyclerViewHolder;

import java.util.List;

/**
 * Created by WIN10 on 2016/2/1.
 */
public class DrugRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    final int defaultType = 1;
    final int emptyType = 0;

    private List<Drug> items;
    private Context context;

    public DrugRecyclerAdapter(Context context, List<Drug> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == emptyType){
            return new RecyclerViewHolder(new EmptyView(context, parent));
        }

        return new RecyclerViewHolder(new DrugItemView(context, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (holder.getItemViewType() == 0)
            return;

        DrugItemView view = (DrugItemView)holder.getIView();
        view.setDrugOtc(items.get(position).getOtc());
        view.setDrugForm(items.get(position).getForm());
        view.setOnClick();

        holder.getBinding().setVariable(BR.drug, items.get(position));
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
