package com.wjd.androidutils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class DeviceHelper {
	/**
	 * 获取设备的Token（Token是一系列唯一标示组成的唯一标示）
	 * @param context	上下文
	 * @return	设备的Token
	 */
    public static String getDeviceToken(Context context) {

        String iemi = getImei(context);
        if (iemi == null || iemi.length() < 14) {//获取失败
            iemi = getMac(context);
        }
        if (iemi.length() < 8) {
            iemi = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        iemi += Build.MANUFACTURER;
        return iemi;
    }
    
    /**
     * 获取设备的IMEI
     * @param context	上下文
     * @return	imei
     */
    public static String getImei(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = manager.getDeviceId();
        if (TextUtils.isEmpty(deviceid)) {
            return "00000000000000";
        }
        return deviceid;
    }
    
    /**
     * 获取设备的MAC
     * @param context 上下文
     * @return mac
     */
    public static String getMac(Context context) {
        try
        {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getMacAddress();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return "";
    }
    /**
     * 获取CPU序列号
     * 
     * @return CPU序列号(16位)
     * 读取失败为"0000000000000000"
     */
   public static String getCPUSerial() {
           String str = "", strCPU = "", cpuAddress = null;
           try {
                   //读取CPU信息
                   Process pp = Runtime.getRuntime().exec("cat/proc/cpuinfo");
                   InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                   LineNumberReader input = new LineNumberReader(ir);
                   //查找CPU序列号
                   for (int i = 1; i < 100; i++) {
                           str = input.readLine();
                           if (str != null) {
                                   //查找到序列号所在行
                                   if (str.indexOf("Serial") > -1) {
                                           //提取序列号
                                           strCPU = str.substring(str.indexOf(":") + 1,
                                                           str.length());
                                           //去空格
                                           cpuAddress = strCPU.trim();
                                           break;
                                   }
                           }else{
                                   //文件结尾
                                   break;
                           }
                   }
           } catch (IOException ex) {
                   //赋予默认值
                   ex.printStackTrace();
           }
           return cpuAddress;
   }	

	/**
	 * 判断手机上面SD卡是否已加载
	 * @return
	 */
	public static boolean isSDCardMounted() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取SD卡根目录
	 * @return
	 */
	public static File getSDCardRoot(){
		return Environment.getExternalStorageDirectory();// SD卡根目录
	}
	
	/**
	 * 创建一个基于SD卡位根目录的相对目录
	 * @param relativePath	相对目录名
	 * @return	新创建的目录
	 */
	public static File creatSDCardRootRPath(String relativePath){
		return new File(getSDCardRoot(), relativePath);
	}
}
