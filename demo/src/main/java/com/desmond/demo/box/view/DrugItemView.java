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
        if (otc == null) otc = "OTHER";

        TextView textView = get(R.id.drug_otc);
        if ("RX".equalsIgnoreCase(otc)) {
            textView.setText("处方");
            textView.setTextColor(ContextCompat.getColor(context, R.color.accent));
            textView.setBackgroundResource(R.drawable.text_view_rx_border);
        } else if ("OTHER".equalsIgnoreCase(otc)) {
            textView.setText("其它");
            textView.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));
            textView.setBackgroundResource(R.drawable.text_view_other_border);
        }
    }

    public void setDrugForm(String form){
        int[][] f = {
                {R.array.select_jiao_lang, R.mipmap.ic_jiaolang},
                {R.array.select_pian_ji, R.mipmap.ic_yaopian},
                {R.array.select_ke_li, R.mipmap.ic_chongji},
                {R.array.select_hun_xuan, R.mipmap.ic_koufuye}
        };

        ImageView imageView = get(R.id.item_drug_form);
        for (int i = 0; i < f.length; i++){
            String[] arr = context.getResources().getStringArray(f[i][0]);
            for (String s : arr) {
                if (form.contains(s)) {
                    imageView.setImageResource(f[i][1]);
                    return;
                }
            }
        }
    }

//    public void setDrugForm(String form) {
//        ImageView imageView = get(R.id.item_drug_form);
//        if (form == null) {
//            imageView.setImageResource(R.mipmap.ic_jiaolang);
//        }
//
//        if (form.contains("胶囊")) {
//            imageView.setImageResource(R.mipmap.ic_jiaolang);
//        } else if (form.contains("片剂")) {
//            imageView.setImageResource(R.mipmap.ic_yaopian);
//        } else if (form.contains("口服液")) {
//            imageView.setImageResource(R.mipmap.ic_koufuye);
//        } else if (form.contains("颗粒剂")) {
//            imageView.setImageResource(R.mipmap.ic_koufuye);
//        }
//    }

    public void setOnClick(final Drug drug, final ClickListener listener) {
        RelativeLayout relativeLayout = get(R.id.item_drug_root);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Drug", "----click-----");
                listener.onClick(v, drug);
            }
        });

        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .items(new String[]{"创建用药计划", "增加剂量", "删除"})
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
