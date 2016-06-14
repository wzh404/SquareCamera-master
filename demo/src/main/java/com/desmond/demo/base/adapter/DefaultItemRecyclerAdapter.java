package com.desmond.demo.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.BR;
import com.desmond.demo.base.RecyclerViewType;
import com.desmond.demo.base.view.AbstractView;
import com.desmond.demo.base.view.DefaultButtonItemView;
import com.desmond.demo.base.view.DefaultItemView;
import com.desmond.demo.base.view.DefaultTextItemView;
import com.desmond.demo.base.view.MarginItemView;
import com.desmond.demo.base.view.SwitchItemView;
import com.desmond.demo.base.view.RecyclerViewHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * Created by WIN10 on 2016/2/1.
 */
public class DefaultItemRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private JsonArray items;
    private Context context;
    private AbstractView.OnSelectListener listener;

    public DefaultItemRecyclerAdapter(Context context, JsonArray items, AbstractView.OnSelectListener listener){
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerViewType.MARGIN.ordinal()){
            MarginItemView view = new MarginItemView(context, parent);

            RecyclerViewHolder holder = new RecyclerViewHolder(view);
            return holder;
        }
        else if (viewType == RecyclerViewType.DEFAULT.ordinal()){
            DefaultItemView view = new DefaultItemView(context, parent);
            RecyclerViewHolder holder = new RecyclerViewHolder(view);

            return holder;
        }
        else if (viewType == RecyclerViewType.SWITCH.ordinal()){
            SwitchItemView view = new SwitchItemView(context, parent);
            RecyclerViewHolder holder = new RecyclerViewHolder(view);

            return holder;
        }
        else if (viewType == RecyclerViewType.TEXT.ordinal()){
            DefaultTextItemView view = new DefaultTextItemView(context, parent);
            RecyclerViewHolder holder = new RecyclerViewHolder(view);

            return holder;
        }
        else if (viewType == RecyclerViewType.BUTTON.ordinal()){
            DefaultButtonItemView view = new DefaultButtonItemView(context, parent);
            RecyclerViewHolder holder = new RecyclerViewHolder(view);

            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        JsonObject obj =  (JsonObject)items.get(position);

        holder.getBinding().setVariable(BR.obj, obj);
        holder.getBinding().executePendingBindings();

        if (holder.getIView() instanceof DefaultItemView){
            DefaultItemView v = (DefaultItemView)holder.getIView();
            v.setListener(listener);
            v.onBindView(obj);
        }
        else if (holder.getIView() instanceof DefaultButtonItemView){
            DefaultButtonItemView v = (DefaultButtonItemView)holder.getIView();

            v.setListener(listener);
            v.onClick(obj);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        JsonObject obj = (JsonObject) items.get(position);
        String type = obj.get("type").getAsString();
        return RecyclerViewType.valueOf(type).ordinal();
    }
}
