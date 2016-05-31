package com.desmond.demo.box.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desmond.demo.R;
import com.desmond.demo.common.AbstractView;
import com.desmond.demo.common.IView;

/**
 * Created by wangzunhui on 2016/5/30.
 */
public class DrugItemView extends AbstractView {
    public DrugItemView(Context context, ViewGroup container){
        super.init(context, container, R.layout.item_drug_box);
    }

    public void setDrugOtc(String otc){
        Log.e("Drug", "otc is " + otc);
        if (otc == null) otc = "OTHER";

        TextView textView = get(R.id.drug_otc);
        if ("RX".equalsIgnoreCase(otc)){
            textView.setText(otc);
            textView.setTextColor(Color.parseColor("#ffe91e63"));
            textView.setBackgroundResource(R.drawable.text_view_rx_border);
        }
        else if ("OTHER".equalsIgnoreCase(otc)){
            textView.setText("其它");
            textView.setTextColor(Color.parseColor("#ff999999"));
            textView.setBackgroundResource(R.drawable.text_view_other_border);
        }
    }
}
