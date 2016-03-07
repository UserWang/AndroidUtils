package com.wjd.androidutils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 */
public class KeyBoardUtils {
    /**
     * 隐藏软键盘
     *
     * @param mActivity
     */
    public static void hide(Activity mActivity) {
        if (null != mActivity && null != mActivity.getCurrentFocus() && isActive(mActivity)) {
            ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 显示输入法软键盘
     *
     * @param
     */
    public static void show(View view) {
        if (null != view) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.RESULT_UNCHANGED_HIDDEN);
        }
    }

    /**
     * 延迟显示软键盘
     *
     * @param view
     * @param duration
     */
    public static void showDelay(final View view, int duration) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                show(view);
            }
        }, duration);
    }


    /**
     * 输入法中的任意视图激活时返回真。
     *
     * @param mActivity
     * @return
     */
    public static boolean isActive(Activity mActivity) {
        if (null != mActivity) {
            return ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).isActive();
        } else {
            return false;
        }
    }
}
