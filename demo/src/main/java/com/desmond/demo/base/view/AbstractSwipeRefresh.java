package com.desmond.demo.base.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.common.util.AndroidUtil;

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

                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

        RecyclerView recyclerView = getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new SpacesItemDecoration(AndroidUtil.dip2px(context, 0.0f)));

        adapter = createAdapter(context);
        recyclerView.setAdapter(adapter);
    }

    public RecyclerView.Adapter getAdapter(){
        return this.adapter;
    }

    public abstract void freshData();
    public abstract RecyclerView.Adapter createAdapter(Context context);
    public abstract SwipeRefreshLayout getSwipeRefresh();
    public abstract RecyclerView getRecyclerView();
}
