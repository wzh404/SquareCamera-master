package com.desmond.demo.plan.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.base.view.AbstractView;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.plan.model.DrugPlan;

import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugPlanItemView extends AbstractView {
    public DrugPlanItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_drug_plan);
    }

    public void setOnClick(final DrugPlan plan, final ClickListener listener) {
        RelativeLayout relativeLayout = get(R.id.item_drug_plan_root);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Drug", "----click-----");
                listener.onClick(v, plan);
            }
        });

        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .items(new String[]{"完成", "续期", "删除"})
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                listener.onLongClick(view, which, plan);
                            }
                        })
                        .backgroundColorRes(R.color.white)
                        .contentColorRes(R.color.black)
                        .show();
                return true;
            }
        });
    }


    public interface ClickListener{
        public void onClick(View view, DrugPlan plan);
        public void onLongClick(View view, int which, DrugPlan plan);
    }
}
