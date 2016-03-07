package com.wjd.androidutils;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang.StringUtils;
import com.wjd.utils.androidutils.R;


/**
 * 吐司工具类
 */
public class ToastUtils {
    private static Toast mToast;
    /**
     * 系统默认短时间吐司
     *
     * @param text 字符串
     */
    public static void shortShow(Context context,String text) {
        if (!StringUtils.isBlank(text)) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 系统默认长时间吐司
     *
     * @param text
     */
    public static void longShow(Context context,String text) {
        if (!StringUtils.isBlank(text)) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();

        }
    }

    /**
     * 系统默认短时间吐司 ,传资源id
     *
     * @param stringsId 字符串id
     */
    public static void shortShow(Context context,int stringsId) {
        try {
            String text = context.getResources().getString(stringsId);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } catch (Resources.NotFoundException exception) {
            LogUtils.d("shortShow: not found stringsId in strings.xml");
        }
    }

    /**
     * 系统默认长时间吐司 ,传资源id
     *
     * @param stringsId 字符串id
     */
    public static void longShow(Context context,int stringsId) {
        try {
            String text = context.getResources().getString(stringsId);
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        } catch (Resources.NotFoundException exception) {
            LogUtils.d("longShow: not found stringsId in strings.xml");
        }
    }

    /**
     * 居中的带图片的toast提示
     * @param str
     * @param imageId
     */
    public static void shorShowWithImage(Context context,String str, int imageId) {
        try {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
            View toastView = mInflater.inflate(R.layout.common_toast_with_image, null);
            Toast toast = new Toast(context);
            toast.setView(toastView);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView mTip = (TextView) toastView.findViewById(R.id.tv_tip);
            TextView mImage = (TextView) toastView.findViewById(R.id.tv_image);
            mImage.setBackgroundResource(imageId);
            if (str!=null &&!str.equals("")) {
                mTip.setText(str);
            }
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Resources.NotFoundException exception) {
            LogUtils.d("longShow: not found stringsId in strings.xml");
        }
    }

    /**
     * 居中的带图片的toast提示
     * @param textId
     * @param imageId
     */
    public static void shorShowWithImage(Context context,int textId, int imageId) {
        try {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
            View toastView = mInflater.inflate(R.layout.common_toast_with_image, null);
            Toast toast = new Toast(context);
            toast.setView(toastView);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView mTip = (TextView) toastView.findViewById(R.id.tv_tip);
            TextView mImage = (TextView) toastView.findViewById(R.id.tv_image);
            mImage.setBackgroundResource(imageId);
            mTip.setText(textId);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Resources.NotFoundException exception) {
            LogUtils.d("longShow: not found stringsId in strings.xml");
        }
    }

    /**
     * 靠下方的纯文字形式的toast提示
     * @param str
     */
    public static void shortShowCommon(Context context,String str) {
        try {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
            View toastView = mInflater.inflate(R.layout.common_short_toast, null);
            Toast toast = new Toast(context);
            toast.setView(toastView);
            toast.setGravity(Gravity.BOTTOM, 0, 70);
            TextView mTip = (TextView) toastView.findViewById(R.id.tv_tip);
            if (str!=null &&!str.equals("")) {
                mTip.setText(str);
            }
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Resources.NotFoundException exception) {
            LogUtils.d("longShow: not found stringsId in strings.xml");
        }
    }

    /**
     * 靠下方的纯文字形式的toast提示
     * @param textId
     */
    public static void shortShowCommon(Context context,int textId) {
        try {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
            View toastView = mInflater.inflate(R.layout.common_short_toast, null);
            Toast toast = new Toast(context);
            toast.setView(toastView);
            toast.setGravity(Gravity.BOTTOM, 0, 70);
            TextView mTip = (TextView) toastView.findViewById(R.id.tv_tip);
            mTip.setText(textId);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Resources.NotFoundException exception) {
            LogUtils.d("longShow: not found stringsId in strings.xml");
        }
    }
    private static void show(Toast toast) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = toast;
        toast.show();
    }
}

