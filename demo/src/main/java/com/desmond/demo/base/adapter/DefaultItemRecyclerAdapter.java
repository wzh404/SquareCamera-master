package com.desmond.demo.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.BR;
import com.desmond.demo.base.RecyclerViewType;
import com.desmond.demo.base.view.DefaultItemView;
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
//    private int currentPosition = 0;

    public DefaultItemRecyclerAdapter(Context context, JsonArray items){
        this.items = items;
        this.context = context;
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
//        else if (viewType == RecyclerViewType.TEL.ordinal()){
//            MyAccountTelView view = new MyAccountTelView(context, parent);
//            RecyclerViewHolder holder = new RecyclerViewHolder(view);
//            return holder;
//        }
//        else if (viewType == RecyclerViewType.LOGOUT.ordinal()){
//            MyAccountLogoutView view = new MyAccountLogoutView(context, parent);
//            RecyclerViewHolder holder = new RecyclerViewHolder(view);
//            return holder;
//        }
//        else if (viewType == RecyclerViewType.LOGIN.ordinal()){
//            this.loginView = new MyAccountLoginView(context, parent);
//            RecyclerViewHolder holder = new RecyclerViewHolder(loginView);
//            return holder;
//        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
//        currentPosition = position;
        JsonObject obj =  (JsonObject)items.get(position);

        holder.getBinding().setVariable(BR.obj, obj);
        holder.getBinding().executePendingBindings();

//        if (holder.getIView() instanceof MyAccountItemView){
//            MyAccountItemView myView = (MyAccountItemView)holder.getIView();
//            myView.initData(obj);
//        }
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
