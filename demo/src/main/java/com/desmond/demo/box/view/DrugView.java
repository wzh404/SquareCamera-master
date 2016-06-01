package com.desmond.demo.box.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.box.adapter.DrugRecyclerAdapter;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.model.DrugBox;
import com.desmond.demo.common.AbstractSwipeRefresh;

import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugView extends AbstractSwipeRefresh {
    private List<Drug> items;
    private CallbackDrugView  callback;

    public DrugView(Context context, ViewGroup container, CallbackDrugView callback) {
        this.callback = callback;
        super.init(context, container, R.layout.fragment_drug);
    }

    @Override
    public void freshData() {
        callback.fresh();
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
        this.items = callback.getItems();
        return new DrugRecyclerAdapter(context, items);
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
        items.add(drug);
        getAdapter().notifyDataSetChanged();
    }

    public interface CallbackDrugView{
        public void fresh();
        public List<Drug> getItems();
    }
}
