package com.desmond.demo.common.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.base.view.WheelView;

/**
 * Created by wangzunhui on 2016/6/14.
 */
public class MaterialDialogUtil {
    public static void showList(View view, String[] items, MaterialDialog.ListCallback callback){
        new MaterialDialog.Builder(view.getContext())
                .items(items)
                .itemsCallback(callback)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .show();
    }

    public static void showMultiChoiceLimited(final View view, String[] items, MaterialDialog.SingleButtonCallback callback){
        new MaterialDialog.Builder(view.getContext())
                .items(items)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(callback)
                .itemsCallbackMultiChoice(new Integer[]{1}, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        boolean allowSelection = which.length <= 3;
                        if (!allowSelection) {
                            Toast.makeText(view.getContext(), "最多可以选择3个", Toast.LENGTH_SHORT).show();
                        }
                        return allowSelection;
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .itemsColorRes(R.color.black)
                .show();
    }
}
