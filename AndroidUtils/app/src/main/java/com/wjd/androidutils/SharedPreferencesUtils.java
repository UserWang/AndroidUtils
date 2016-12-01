package com.android.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreference工具类
 */
public class SharedPreferencesUtils {
	private SharedPreferences mSp;
	private String mName = "magic";
	private static SharedPreferencesUtils mInstance;

	public static SharedPreferencesUtils getInstance(Context context){
		if (null == mInstance){
			synchronized (SharedPreferencesUtils.class){
				if (null == mInstance){
					mInstance = new SharedPreferencesUtils(context);
				}
			}
		}
		return mInstance;
	}

	private SharedPreferencesUtils(){
	}

	private SharedPreferencesUtils(Context context){
		mSp = context.getSharedPreferences(mName,Context.MODE_PRIVATE);
	}

	public void putStringValue(String key, String value){
		Editor editor = mSp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void putIntValue(String key, int value){
		Editor editor = mSp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void putBooleanValue(String key, boolean  enable){
		Editor editor = mSp.edit();
		editor.putBoolean(key, enable);
		editor.commit();
	}

	public String getStringValue(String key){
		return mSp.getString(key, null);
	}

	public int getIntValue(String key){
		return mSp.getInt(key, 0);
	}

	public boolean getBooleanValue(String key){
		return mSp.getBoolean(key, false);
	}

}
