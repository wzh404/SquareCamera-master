package com.desmond.demo.base.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.desmond.demo.R;
import com.desmond.demo.common.util.AndroidUtil;
import com.google.gson.JsonObject;


/**
 * Created by WIN10 on 2016/2/1.
 */
public class DefaultItemView extends AbstractView {
    public final static String TAG_LOGIN = "tag_item";
    private Context context;
    private String code;

    public DefaultItemView(Context context, ViewGroup container) {
        super.init(context, container, R.layout.item_default);
        this.context = context;
    }

//    public void setMarginBottom() {
//        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getView().getLayoutParams();
//        layoutParams.setMargins(0, 0, 0, AndroidUtil.dip2px(context, 10.0f));
//    }
//
//    public void initData(JsonObject obj) {
//        this.code = obj.get("code").getAsString();
//
//        setItemOnClick();
//    }
//
//    public String getCode(){
//        return code;
//    }
//
//    public void setItemOnClick() {
//
//    }
}
