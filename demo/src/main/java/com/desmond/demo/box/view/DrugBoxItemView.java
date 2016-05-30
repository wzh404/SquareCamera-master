package com.desmond.demo.box.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.common.AbstractView;
import com.desmond.demo.common.IView;

/**
 * Created by wangzunhui on 2016/5/30.
 */
public class DrugBoxItemView extends AbstractView {
    public DrugBoxItemView(Context context, ViewGroup container){
        super.init(context, container, R.layout.item_drug_box);
    }
}
