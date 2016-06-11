package com.desmond.demo.base.view;

import android.content.Context;
import android.view.ViewGroup;

import com.desmond.demo.R;


/**
 * Created by WIN10 on 2016/2/1.
 */
public class DefaultButtonItemView extends AbstractView {
    private Context context;

    public DefaultButtonItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_default_button);
        this.context = context;
    }
}
