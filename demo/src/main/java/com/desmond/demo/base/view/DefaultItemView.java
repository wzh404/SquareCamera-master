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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;


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
        final String code = object.get("code").getAsString();
        RelativeLayout layout = get(R.id.item_default_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("dosage".equalsIgnoreCase(code)){
                    showDosage(object);
                }
                else {
                    showList(object);
                }
            }
        });
    }

    private void showDosage(JsonObject object){
        final String code = object.get("code").getAsString();

        MaterialDialog dialog = new MaterialDialog.Builder(this.context)
                .title(R.string.dialog_dosage_title)
                .customView(R.layout.dialog_drug_dosage, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        TextView tv = get(R.id.item_my_desc);
                        NumberPicker picker = (NumberPicker)dialog.getCustomView().findViewById(R.id.dosage_value);
                        WheelView wheelView = (WheelView)dialog.getCustomView().findViewById(R.id.main_wv);
                        tv.setText(picker.getValue() + wheelView.getSeletedItem());

                        if (getListener() != null){
                            getListener().onSelected(code, picker.getValue() + "", wheelView.getSeletedItem());
                        }
                    }
                })
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .titleColorRes(R.color.primary)
                .build();
        NumberPicker picker = (NumberPicker)dialog.getCustomView().findViewById(R.id.dosage_value);
        picker.setMinValue(1);
        picker.setMaxValue(100);
        picker.setValue(2);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        WheelView wheelView = (WheelView)dialog.getCustomView().findViewById(R.id.main_wv);
        wheelView.setOffset(1);
        wheelView.setItems(Arrays.asList(context.getResources().getStringArray(R.array.spinner_dosage)));
        wheelView.setSelection(object.get("dosage").getAsString());
        dialog.show();
    }

    private void showList(JsonObject object){
        if (object.get("select") == null || object.get("select").getAsJsonArray().isJsonNull()){
            return;
        }

        final String code = object.get("code").getAsString();
        int  mores = object.get("select").getAsJsonArray().size();
        final String[] selection = new String[mores];
        int i = 0;
        for (JsonElement element : object.get("select").getAsJsonArray()){
            selection[i++] = element.getAsString();
        }

        new MaterialDialog.Builder(this.context)
                .items(selection)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        TextView tv = get(R.id.item_my_desc);
                        tv.setText(text);

                        if (getListener() != null){
                            getListener().onSelected(code, text.toString());
                        }
                    }
                })
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .show();
    }
}
