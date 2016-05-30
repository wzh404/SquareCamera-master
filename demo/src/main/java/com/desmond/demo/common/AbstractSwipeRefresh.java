package com.desmond.demo.common;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.box.adapter.DrugBoxRecyclerAdapter;
import com.desmond.demo.common.util.AndroidUtil;

import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public abstract class AbstractSwipeRefresh<E> extends AbstractView{
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.Adapter adapter;

    public void init(Context context, ViewGroup container, int res){
        super.init(context, container, res);
        initSwipeRefresh(context);
    }

    public void initSwipeRefresh(Context context){
        swipeRefreshLayout = getSwipeRefresh();
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        freshData();
                    }
                });

        RecyclerView recyclerView = getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new SpacesItemDecoration(AndroidUtil.dip2px(context, 10.0f)));

        adapter = getAdapter(context);
        recyclerView.setAdapter(adapter);
    }

    public abstract void freshData();
    public abstract RecyclerView.Adapter getAdapter(Context context);
    public abstract SwipeRefreshLayout getSwipeRefresh();
    public abstract RecyclerView getRecyclerView();
}
