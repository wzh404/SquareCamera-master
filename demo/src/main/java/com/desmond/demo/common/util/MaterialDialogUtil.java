package com.desmond.demo.common.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.base.view.IView;
import com.desmond.demo.base.view.WheelView;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.GregorianCalendar;

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

    public static void showPlanTimeAndDosage(Context context, String time, int dosage, String unit, final MaterialDialogUtil.TimeAndDosageCallback callback){
        MaterialDialog.SingleButtonCallback singleButtonCallback = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                TimePicker timePicker = (TimePicker)dialog.getCustomView().findViewById(R.id.plan_temp_time);
                AppCompatEditText editText = (AppCompatEditText)dialog.getCustomView().findViewById(R.id.plan_temp_dosage);
                int dosage = Integer.parseInt(editText.getText().toString());

                String time = "00:00";
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    time = String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                }
                else{
                    time = String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute());
                }

                callback.onClick(time, dosage);
            }
        };

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.dialog_plan_time)
                .customView(R.layout.dialog_plan_temp, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(singleButtonCallback)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .titleColorRes(R.color.primary)
                .build();
        TimePicker timePicker = (TimePicker)dialog.getCustomView().findViewById(R.id.plan_temp_time);
        timePicker.setIs24HourView(true);

        String t[] = time.split(":");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            timePicker.setCurrentHour(Integer.parseInt(t[0]));
            timePicker.setCurrentMinute(Integer.parseInt(t[1]));
        }
        else{
            timePicker.setHour(Integer.parseInt(t[0]));
            timePicker.setMinute(Integer.parseInt(t[1]));
        }

        AppCompatEditText editText = (AppCompatEditText)dialog.getCustomView().findViewById(R.id.plan_temp_dosage);
        editText.setText(dosage + "");

        TextView textView = (TextView)dialog.getCustomView().findViewById(R.id.plan_temp_unit);
        textView.setText(unit);
        dialog.show();
    }

    public static void showDate(Context context, final MaterialDialogUtil.DateCallback callback){
        MaterialDialog.SingleButtonCallback singleButtonCallback = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                DatePicker datePicker = (DatePicker)dialog.getCustomView().findViewById(R.id.plan_start_date);
                Date date = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()).getTime();
                callback.onClick(date);
            }
        };
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.dialog_plan_date)
                .customView(R.layout.dialog_plan_date, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(singleButtonCallback)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .titleColorRes(R.color.primary)
                .build();

        dialog.show();
    }

    public static void showDosage(Context context, Integer dosage, String unit, boolean enabled, final MaterialDialogUtil.DosageCallback callback){
        final String[] units = context.getResources().getStringArray(R.array.spinner_dosage);

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.dialog_dosage_title)
                .customView(R.layout.dialog_drug_dosage, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        NumberPicker picker = (NumberPicker)dialog.getCustomView().findViewById(R.id.dosage_value);
                        NumberPicker unitPicker = (NumberPicker)dialog.getCustomView().findViewById(R.id.dosage_unit);
                        callback.onClick(picker.getValue(), units[unitPicker.getValue() - 1]);
                    }
                })
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .titleColorRes(R.color.primary)
                .build();
        NumberPicker picker = (NumberPicker)dialog.getCustomView().findViewById(R.id.dosage_value);
        picker.setMinValue(1);
        picker.setMaxValue(100);
        picker.setValue(dosage);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        NumberPicker unitPicker = (NumberPicker)dialog.getCustomView().findViewById(R.id.dosage_unit);
        unitPicker.setDisplayedValues(units);
        unitPicker.setMinValue(1);
        unitPicker.setMaxValue(units.length);
        for (int i = 0; i < units.length; i++) {
            if (unit.equalsIgnoreCase(units[i]))
                unitPicker.setValue(i + 1);
        }
        unitPicker.setEnabled(enabled);

        unitPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        dialog.show();
    }

    public interface TimeAndDosageCallback{
        public void onClick(String time, int dosage);
    }

    public interface DateCallback{
        public void onClick(Date date);
    }

    public interface DosageCallback{
        public void onClick(int dosage, String unit);
    }
}
