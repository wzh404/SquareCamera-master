package com.desmond.demo.base.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.common.util.AndroidUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * Created by WIN10 on 2016/2/1.
 */
public class DefaultItemView extends AbstractView {
    private Context context;

    public DefaultItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_default);
        this.context = context;
    }

    public void onBindView(JsonObject object){
        if (object.get("select") == null || object.get("select").getAsJsonArray().isJsonNull()){
            return;
        }
//        final String code = object.get("code").getAsString();
        int  mores = object.get("select").getAsJsonArray().size();
        final String[] selection = new String[mores];
        int i = 0;
        for (JsonElement element : object.get("select").getAsJsonArray()){
            selection[i++] = element.getAsString();
        }
        RelativeLayout layout = get(R.id.item_default_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .items(selection)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                TextView tv = get(R.id.item_my_desc);
                                tv.setText(text);
                            }
                        })
                        .backgroundColorRes(R.color.white)
                        .contentColorRes(R.color.black)
                        .show();
            }
        });
    }
}
