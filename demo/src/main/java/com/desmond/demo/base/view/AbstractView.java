package com.desmond.demo.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WIN10 on 2015/11/14.
 */
public class AbstractView implements IView {
    protected View view;
    private Context context;
    private OnSelectListener listener;

    public void init(Context context, ViewGroup container, int res){
        this.context = context;
        view = LayoutInflater.from(context).inflate(res, container, false);
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public <T extends View> T get(int id){
        return (T)getView().findViewById(id);
    }

    public void setListener(OnSelectListener listener){
        this.listener = listener;
    }

    public OnSelectListener getListener(){
        return listener;
    }
    public interface OnSelectListener{
        public void onSelected(String ...arg);
    }
}
