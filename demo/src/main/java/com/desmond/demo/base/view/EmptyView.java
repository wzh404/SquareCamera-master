package com.desmond.demo.base.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.desmond.demo.R;


/**
 * Created by WIN10 on 2016/2/1.
 */
public class EmptyView extends AbstractView {
    public EmptyView(Context context, ViewGroup container){
        super.init(context, container, R.layout.item_empty);

//        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//        Point point = new Point();
//        windowManager.getDefaultDisplay().getSize(point);
//        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)getView().getLayoutParams();
//        layoutParams.height = point.y;
//        getView().setLayoutParams(layoutParams);
    }

    public void setHintText(int img, int text){
        TextView textView = get(R.id.empty_hint);
        textView.setText(text);

        ImageView imageView = get(R.id.empty_image);
        imageView.setImageResource(img);
    }
}
