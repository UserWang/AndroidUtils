package com.wjd.androidutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 */
public class NetWorkUtils {

    /**
     * 判断当前手机是否联网
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo == null ? false : mNetworkInfo.isAvailable();
    }

    /**
     * 判断当前联网状态是否为Wifi
     *
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWiFiNetworkInfo == null ? false : mWiFiNetworkInfo.isAvailable();
    }

    /**
     * 判断当前手机运营商网络是否可用
     *
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mMobileNetworkInfo == null ? false : mMobileNetworkInfo.isAvailable();

    }

    /**
     * 检查网络连接 需要Toast提示
     *
     * @return true 网络连接正常
     */
    public static boolean checkNetWork(Context context) {
        if (!NetWorkUtils.isNetworkConnected(context)) {
            ToastUtils.shortShow(context, "当前网络无连接，请检查您的网络设置");
            return false;
        }
        return true;
    }

}
