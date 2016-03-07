package com.wjd.androidutils;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LogUtils {

    private static Handler LOG_HANDLER = null;
    private static final int MSG_WHAT_EXIT = 0;
    private static final int MSG_WHAT_LOG = 1;
    private static final int MSG_WHAT_BUILD_STREAM = 2;
    private static final String TAG = "TAG";
    private static final String MSG = "MSG";
    private static FileOutputStream LOG_STREAM = null;
    private static boolean IS_DEBUG = false;
    private static final String DEFAULT_TAG = "AndroidUtils";

    public static void setDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }

    public static void startLogToSdcard() {
        if (LOG_HANDLER == null) {
            HandlerThread handlerThread = new HandlerThread("LogToSdcardThread");
            handlerThread.start();
            LOG_HANDLER = new LogHandler(handlerThread.getLooper());
            Message msg = LOG_HANDLER.obtainMessage();
            if (msg != null) {
                msg.what = MSG_WHAT_BUILD_STREAM;
                LOG_HANDLER.sendMessage(msg);
            }
        }
    }

    public static void stopLogToSdcard() {
        if (LOG_HANDLER == null) {
            return;
        }

        Message msg = LOG_HANDLER.obtainMessage();
        if (msg != null) {
            msg.what = MSG_WHAT_EXIT;
            LOG_HANDLER.sendMessage(msg);
            LOG_HANDLER = null;
        }
    }


    /**
     * @return true opened
     * false closed
     */
    public static boolean getLogToSdcardStatus() {
        return LOG_HANDLER != null;
    }

    public static class LogHandler extends Handler {
        public LogHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                return;
            }

            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_LOG:
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        String tag = bundle.getString(TAG);
                        String logMsg = bundle.getString(MSG);
                        writeLogToStream(LOG_STREAM, tag, logMsg);
                    }
                    break;
                case MSG_WHAT_BUILD_STREAM:
                    LOG_STREAM = buildLogOutputStream();
                    break;
                case MSG_WHAT_EXIT:
                    if (LOG_STREAM != null) {
                        try {
                            LOG_STREAM.close();
                            LOG_STREAM = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Looper looper = this.getLooper();
                    if (looper != null) {
                        looper.quit();
                    }
                    break;
            }
        }
    }

    public static void logToSdcard(String tag, String msg) {
        if (LOG_HANDLER != null) {
            Message logMsg = LOG_HANDLER.obtainMessage();
            if (logMsg != null) {
                logMsg.what = MSG_WHAT_LOG;
                Bundle data = new Bundle();
                data.putString(TAG, tag);
                data.putString(MSG, msg);
                logMsg.setData(data);
                LOG_HANDLER.sendMessage(logMsg);
            }
        }
    }

    public static void d(String msg) {
        d(getTag(), msg);
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, msg);
        }

        if (LOG_HANDLER != null) {
            logToSdcard(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if(IS_DEBUG){
            Log.d(tag, msg, tr);
        }

        if (LOG_HANDLER != null) {
            logToSdcard(tag, msg);
        }
    }

    public static void e(String msg) {
        e(getTag(), msg);
    }

    public static void e(String msg, Throwable t) {
        String stackTraceString = Log.getStackTraceString(t);
        e(getTag(), msg + '\n' + stackTraceString);
    }

    private static String getTag() {
        try {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            String simpleName = stackTrace[2].getClassName();
            return simpleName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_TAG;
    }

    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            Log.e(tag, msg);
        }

        if (LOG_HANDLER != null) {
            logToSdcard(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if(IS_DEBUG){
            Log.e(tag, msg, tr);
        }

        if (LOG_HANDLER != null) {
            logToSdcard(tag, msg);
        }
    }

    public static void w(Throwable throwable) {
        w(getTag(), Log.getStackTraceString(throwable));
    }

    public static void w(String tag, String msg) {
        if (IS_DEBUG) {
            Log.e(tag, msg);
        }

        if (LOG_HANDLER != null) {
            logToSdcard(tag, msg);
        }
    }

    public static void v(String msg) {
        v(getTag(), msg);
    }

    public static void v(String tag, String msg) {
        if (IS_DEBUG) {
            Log.v(tag, msg);
        }

        if (LOG_HANDLER != null) {
            logToSdcard(tag, msg);
        }
    }

    public static void i(String msg) {
        i(getTag(), msg);
    }

    public static void i(String tag, String msg) {
        if (IS_DEBUG) {
            Log.i(tag, msg);
        }

        if (LOG_HANDLER != null) {
            logToSdcard(tag, msg);
        }
    }

    public static void i(String tag, Object object) {
        if (IS_DEBUG) {
            Log.i(tag, (object == null ? "null" : object) + "");
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if(IS_DEBUG){
            Log.i(tag, msg, tr);
        }

        if (LOG_HANDLER != null) {
            logToSdcard(tag, msg);
        }
    }

    private static void writeLogToStream(FileOutputStream outputStream, String tag, String msg) {
        if (outputStream == null || tag == null || msg == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append(df.format(new Date()));
        sb.append("  ");
        sb.append(tag);
        sb.append("  ");
        sb.append(msg);
        sb.append("\n");
        try {
            outputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FileOutputStream buildLogOutputStream() {
        FileOutputStream retOutputStream = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            final File path = Environment.getExternalStorageDirectory();
            if (path != null) {
                String suffix = getTimestampForFileName();
                String fileName = "logs_txl_" + suffix;
                final String logfile = pathAppend(path.getAbsolutePath(), fileName);
                try {
                    File file = new File(logfile);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    retOutputStream = new FileOutputStream(file, true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return retOutputStream;
    }

    public static String getTimestampForFileName() {
        long time = System.currentTimeMillis();
        StringBuilder buf = new StringBuilder();
        buf.append(DateFormat.format("MMdd", time));
        buf.append("_");
        buf.append(time);
        return buf.toString();
    }

    public static String pathAppend(String path, String more) {
        StringBuilder buffer = new StringBuilder(path);
        if (!path.endsWith("/")) {
            buffer.append('/');
        }
        buffer.append(more);

        return buffer.toString();
    }
}
