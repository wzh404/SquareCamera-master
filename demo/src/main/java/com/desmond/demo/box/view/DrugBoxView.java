package com.desmond.demo.box.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.box.adapter.DrugBoxRecyclerAdapter;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.model.DrugBox;
import com.desmond.demo.common.AbstractSwipeRefresh;
import com.desmond.demo.common.AbstractView;
import com.desmond.demo.common.SpacesItemDecoration;
import com.desmond.demo.common.util.AndroidUtil;

import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugBoxView extends AbstractSwipeRefresh {
    private CallbackDrugBoxView  callback;
    public DrugBoxView(Context context, ViewGroup container, CallbackDrugBoxView callback) {
        this.callback = callback;
        super.init(context, container, R.layout.item_drug_box);
    }

    @Override
    public void freshData() {
        callback.fresh();
    }

    @Override
    public RecyclerView.Adapter getAdapter(Context context) {
        return new DrugBoxRecyclerAdapter(context, callback.getDrugBoxes());
    }

    @Override
    public SwipeRefreshLayout getSwipeRefresh() {
        return get(R.id.drug_box_swipe_refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return get(R.id.drug_box_recycler_view);
    }

    public interface CallbackDrugBoxView{
        public void fresh();
        public List<DrugBox> getDrugBoxes();
    }
}
