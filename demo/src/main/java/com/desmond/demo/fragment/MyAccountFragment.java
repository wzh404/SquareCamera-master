package com.desmond.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.base.adapter.DefaultItemRecyclerAdapter;
import com.desmond.demo.base.view.SpacesItemDecoration;
import com.desmond.demo.common.util.AndroidUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * 用药
 *
 * Created by wangzunhui on 2016/6/2.
 */
public class MyAccountFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_my_account, null);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("账户中心");
        toolbar.inflateMenu(R.menu.menu_my_account);

        String json = AndroidUtil.getFromAssets(getContext(), "my_account.json");
        JsonArray items = (new JsonParser()).parse(json).getAsJsonArray();

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.my_account_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));

        recyclerView.setAdapter(new DefaultItemRecyclerAdapter(getContext(), items, null));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
