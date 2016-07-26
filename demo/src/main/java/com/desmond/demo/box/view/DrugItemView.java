package com.desmond.demo.box.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.base.view.AbstractView;
import com.desmond.demo.box.model.Drug;

/**
 * Created by wangzunhui on 2016/5/30.
 */
public class DrugItemView extends AbstractView {
    private Context context;

    public DrugItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_drug_box);
        this.context = context;
    }

    public void setDrugOtc(String otc) {
        if (otc == null || "".equalsIgnoreCase(otc)) otc = "OTHER";

        TextView textView = get(R.id.drug_otc);
        if ("RX".equalsIgnoreCase(otc)) {
            textView.setText("处方");
            textView.setTextColor(ContextCompat.getColor(context, R.color.accent));
            textView.setBackgroundResource(R.drawable.text_view_rx_border);
        }
        else if ("OTC".equalsIgnoreCase(otc)) {
            textView.setText("OTC");
            textView.setTextColor(ContextCompat.getColor(context, R.color.otc));
            textView.setBackgroundResource(R.drawable.text_view_otc_border);
        }
        else if ("OTHER".equalsIgnoreCase(otc)) {
            textView.setText("其它");
            textView.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));
            textView.setBackgroundResource(R.drawable.text_view_other_border);
        }
    }

    public void setDrugIcon(String icon){
        int res = context.getResources().getIdentifier("ic_" + icon, "mipmap", context.getPackageName());
        ImageView imageView = get(R.id.item_drug_form);
        imageView.setImageResource(res);
    }

    public void setOnClick(final Drug drug, final ClickListener listener) {
        RelativeLayout relativeLayout = get(R.id.item_drug_root);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("Drug", "----click-----");
                listener.onClick(v, drug);
            }
        });

        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .items(new String[]{"创建用药计划", "删除该药品"})
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                listener.onLongClick(view, which, drug);
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
        public void onClick(View view, Drug drug);
        public void onLongClick(View view, int which, Drug drug);
    }
}
