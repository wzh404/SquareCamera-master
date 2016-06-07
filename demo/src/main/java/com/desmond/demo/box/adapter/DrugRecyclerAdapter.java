package com.desmond.demo.box.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desmond.demo.BR;
import com.desmond.demo.R;
import com.desmond.demo.base.view.EmptyView;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.view.DrugItemView;
import com.desmond.demo.base.view.RecyclerViewHolder;

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
            EmptyView v = new EmptyView(context, parent);
            v.setHintText(R.mipmap.empty_pill, R.string.empty_pill);
            return new RecyclerViewHolder(v);
        }

        return new RecyclerViewHolder(new DrugItemView(context, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (holder.getItemViewType() == emptyType)
            return;

        Drug drug = items.get(position);
        DrugItemView view = (DrugItemView)holder.getIView();
        view.setDrugOtc(drug.getOtc());
        view.setDrugForm(drug.getForm());
        view.setOnClick();
//        ((TextView)view.get(R.id.drug_no)).setText("国药准字" + drug.getCode());

        holder.getBinding().setVariable(BR.drug, drug);
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
