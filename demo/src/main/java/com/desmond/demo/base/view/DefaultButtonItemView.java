package com.desmond.demo.base.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.desmond.demo.R;
import com.google.gson.JsonObject;


/**
 * Created by WIN10 on 2016/2/1.
 */
public class DefaultButtonItemView extends AbstractView {
    private Context context;

    public DefaultButtonItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_default_button);
        this.context = context;
    }

    public void onClick(JsonObject object){
        final String code = object.get("code").getAsString();

        LinearLayout layout = get(R.id.item_default_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getListener() != null) {
                    getListener().onSelected(DefaultButtonItemView.this, code, 0, "");
                }
            }
        });
    }
}
