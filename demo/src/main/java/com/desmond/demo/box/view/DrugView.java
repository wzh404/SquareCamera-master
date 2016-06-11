package com.desmond.demo.box.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.box.adapter.DrugRecyclerAdapter;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.base.view.AbstractSwipeRefresh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugView extends AbstractSwipeRefresh {
    private List<Drug> items;
//    private CallbackDrugView  callback;
    private DrugItemView.ClickListener listener;

    public DrugView(Context context, ViewGroup container, DrugItemView.ClickListener listener) {
//        this.callback = callback;
        this.listener = listener;
        super.init(context, container, R.layout.fragment_drug_box);
    }

    @Override
    public void freshData() {
//        callback.fresh();
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
//        this.items = callback.getItems();
        items = new ArrayList<>();

        Drug drug = new Drug();
        drug.setId(0);
        items.add(drug);
        return new DrugRecyclerAdapter(context, items, listener);
    }

    @Override
    public SwipeRefreshLayout getSwipeRefresh() {
        return get(R.id.drug_box_swipe_refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return get(R.id.drug_box_recycler_view);
    }

    public void addItem(Drug drug){
        Log.e("Drug", "----addItem1----");
        if (items.size() == 1 && items.get(0).getId().intValue() == 0){
            items.clear();
        }

        items.add(0, drug);
        getAdapter().notifyDataSetChanged();
    }

    public void addItem(List<Drug> drugs){
        if (drugs.size() == 0)
            return;

        Log.e("Drug", "----addItem2----");
        if (items.size() == 1 && items.get(0).getId().intValue() == 0){
            items.clear();
        }

        items.addAll(0, drugs);
        getAdapter().notifyDataSetChanged();
    }

    public void deleteItem(Drug drug){
        Log.e("Drug", "----deleteItem----");
        items.remove(drug);
        getAdapter().notifyDataSetChanged();
    }

//    public interface CallbackDrugView{
//        public void fresh();
//        public List<Drug> getItems();
//    }
}
