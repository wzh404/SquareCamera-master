package com.desmond.demo.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.common.util.AndroidUtil;
import com.desmond.demo.common.util.MaterialDialogUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;

import io.realm.Realm;


/**
 * Created by WIN10 on 2016/2/1.
 */
public class DefaultItemView extends AbstractView {
    private Context context;

    public DefaultItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_default);
        this.context = context;
    }

    public void onBindView(final JsonObject object){
        final IView view = this;
        final String code = object.get("code").getAsString();

        RelativeLayout layout = get(R.id.item_default_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object.get("select") == null || object.get("select").isJsonNull()){
                    return;
                }

                if (object.get("select").isJsonArray()) {
                    showList(object);
                }
                else if ("dosage".equalsIgnoreCase(object.get("select").getAsString())){
                    String unit = object.get("dosage").getAsString();
                    String stock = object.get("stock").getAsString();

                    MaterialDialogUtil.showDosage(context, Integer.parseInt(stock), unit, true, new MaterialDialogUtil.DosageCallback() {
                        @Override
                        public void onClick(final int dosage, final String unit) {
//                            TextView tv = get(R.id.item_my_desc);
//                            tv.setText(dosage + unit);
                            object.addProperty("desc", dosage + unit);
                            if (getListener() != null){
                                getListener().onSelected(view, code, 0, dosage + "", unit);
                            }
                        }
                    });
                }
                else{
                    if (getListener() != null){
                        getListener().onSelected(view, code, 0, "");
                    }
                }
            }
        });
    }

    private void showList(final JsonObject object){
        final String code = object.get("code").getAsString();
        int  mores = object.get("select").getAsJsonArray().size();
        final String[] selection = new String[mores];
        int i = 0;
        for (JsonElement element : object.get("select").getAsJsonArray()){
            selection[i++] = element.getAsString();
        }

        final IView view = this;
        MaterialDialog.ListCallback callback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View v, int which, CharSequence text) {
//                TextView tv = get(R.id.item_my_desc);
                object.addProperty("desc", text.toString());
//                tv.setText(text);

                if (getListener() != null){
                    getListener().onSelected(view, code, which, text.toString());
                }
            }
        };

        MaterialDialogUtil.showList(this.getView(), null, selection, callback);
    }
}
