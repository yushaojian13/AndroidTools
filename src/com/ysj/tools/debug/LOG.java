
package com.ysj.tools.debug;

import java.util.regex.Pattern;

import android.util.Log;

public class LOG {

    public static final int NULL = 6;
    public static final int VERBOSE = 5;
    public static final int DEBUG = 4;
    public static final int INFO = 3;
    public static final int WARN = 2;
    public static final int ERROR = 1;
    public static final int ASSERT = 0;

    private static String TAG = "LockAir";
    private static boolean isLog = false;
    public static boolean isFormat = true;
    private static int LOG_LEVEL = ASSERT;

    public static void enableLog(boolean flag) {
        isLog = flag;
        if (isLog) {
            LOG_LEVEL = ASSERT;
        } else {
            LOG_LEVEL = NULL;
        }
    }

    public static void setLogLevel(int level) {
        LOG_LEVEL = level;
    }

    public static void v(Object msg) {
        if (isLog) {
            Log.v(TAG, format(msg));
        }
    }

    public static void d(Object msg) {
        if (isLog) {
            Log.d(TAG, format(msg));
        }
    }

    public static void i(Object msg) {
        if (isLog) {
            Log.i(TAG, format(msg));
        }
    }

    public static void w(Object msg) {
        if (isLog) {
            Log.w(TAG, format(msg));
        }
    }

    public static void e(Object msg) {
        if (isLog) {
            Log.e(TAG, format(msg));
        }
    }

    public static void v(String tag, Object msg) {
        if (LOG_LEVEL < VERBOSE) {
            Log.v(tag, format(msg));
        }
    }

    public static void d(String tag, Object msg) {
        if (LOG_LEVEL < DEBUG) {
            Log.d(tag, format(msg));
        }
    }

    public static void i(String tag, Object msg) {
        if (LOG_LEVEL < INFO) {
            Log.i(tag, format(msg));
        }
    }

    public static void w(String tag, Object msg) {
        if (LOG_LEVEL < WARN) {
            Log.w(tag, format(msg));
        }
    }

    public static void e(String tag, Object msg) {
        if (LOG_LEVEL < ERROR) {
            Log.e(tag, format(msg));
        }
    }

    private static String format(Object msg) {
        if (isFormat) {
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            StackTraceElement e = stacktrace[4];
            String methodName = e.getMethodName();
            String className = e.getClassName();
            String[] splits = className.split(Pattern.quote("."));
            return String.format("%s %s %s ==> %s", Thread.currentThread().getId(), splits[splits.length - 1], methodName, msg);
        }
        return msg.toString();
    }
}
