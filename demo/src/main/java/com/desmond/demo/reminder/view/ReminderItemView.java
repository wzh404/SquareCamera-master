package com.desmond.demo.reminder.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.base.view.AbstractView;
import com.desmond.demo.plan.model.DrugPlan;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class ReminderItemView extends AbstractView {
    public ReminderItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_reminder);
    }
}
