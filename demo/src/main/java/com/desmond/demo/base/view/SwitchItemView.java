package com.desmond.demo.base.view;

import android.content.Context;
import android.view.ViewGroup;

import com.desmond.demo.R;


/**
 * Created by WIN10 on 2016/2/1.
 */
public class SwitchItemView extends AbstractView {
    private Context context;

    public SwitchItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_switch);
        this.context = context;
    }
}
