package com.desmond.demo.base.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * Created by WIN10 on 2016/2/1.
 */
public class DefaultTextItemView extends AbstractView {
    private Context context;

    public DefaultTextItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_default_text);
        this.context = context;
    }
}
