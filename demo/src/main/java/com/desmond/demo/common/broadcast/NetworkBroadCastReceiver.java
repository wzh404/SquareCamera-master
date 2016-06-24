package com.desmond.demo.common.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.desmond.demo.common.util.NetworkUtil;

/**
 * Created by wangzunhui on 2016/6/22.
 */
public class NetworkBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.e("Drug", "wifi:" + NetworkUtil.isWifiConnected(context) + " mobile:" + NetworkUtil.isMobileConnected(context) + " - " + NetworkUtil.getNetworkType(context));
        }
    }
}
