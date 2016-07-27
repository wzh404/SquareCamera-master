package com.desmond.demo.plan.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by wangzunhui on 2016/7/27.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Drug", "______Time______" + intent.getStringExtra("ID"));
    }
}
