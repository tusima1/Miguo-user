package com.miguo.live.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by didik on 2016/9/8.
 */
public abstract class NetWorkStateReceiver extends BroadcastReceiver {
    public static final IntentFilter NETWORK_FILTER = new IntentFilter(
            ConnectivityManager.CONNECTIVITY_ACTION);
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo gprsInfo = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        // 判断是否是Connected事件
        boolean wifiConnected = false;
        boolean gprsConnected = false;
        if (wifiInfo != null && wifiInfo.isConnected()) {
            wifiConnected = true;
        }
        if (gprsInfo != null && gprsInfo.isConnected()) {
            gprsConnected = true;
        }
        if (wifiConnected || gprsConnected) {
            onConnected();
            return;
        }

        // 判断是否是Disconnected事件，注意：处于中间状态的事件不上报给应用！上报会影响体验
        boolean wifiDisconnected = false;
        boolean gprsDisconnected = false;
        if (wifiInfo == null || wifiInfo != null
                && wifiInfo.getState() == NetworkInfo.State.DISCONNECTED) {
            wifiDisconnected = true;
        }
        if (gprsInfo == null || gprsInfo != null
                && gprsInfo.getState() == NetworkInfo.State.DISCONNECTED) {
            gprsDisconnected = true;
        }
        if (wifiDisconnected && gprsDisconnected) {
            onDisconnected();
            return;
        }
    }
    protected abstract void onDisconnected();

    protected abstract void onConnected();
}
