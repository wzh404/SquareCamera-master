package com.desmond.demo.reminder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.BR;
import com.desmond.demo.R;
import com.desmond.demo.base.view.EmptyView;
import com.desmond.demo.base.view.RecyclerViewHolder;
import com.desmond.demo.reminder.model.Reminder;
import com.desmond.demo.reminder.view.ReminderItemView;

import java.util.List;

/**
 * Created by WIN10 on 2016/2/1.
 */
public class ReminderRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    final int defaultType = 1;
    final int emptyType = 0;

    private List<Reminder> items;
    private Context context;
//    private DrugPlanItemView.ClickListener clickListener;

    public ReminderRecyclerAdapter(Context context, List<Reminder> items){
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

        return new RecyclerViewHolder(new ReminderItemView(context, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (holder.getItemViewType() == emptyType)
            return;

//        Reminder plan = items.get(position);
//        ReminderItemView view = (ReminderItemView)holder.getIView();
////        view.setOnClick(plan, clickListener);

        holder.getBinding().setVariable(BR.reminder, items.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getTime() == null)
            return emptyType;

        return defaultType;
    }
}
