
package com.ysj.tools.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {
    private static volatile SharedPreferences sp;

    public static void init(Context context) {
        if (sp == null) {
            synchronized (SPHelper.class) {
                if (sp == null) {
                    sp = context.getSharedPreferences("LockAir", Context.MODE_PRIVATE);
                }
            }
        }
    }

    public static boolean get(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public static void put(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    public static int get(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public static void put(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public static float get(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public static void put(String key, float value) {
        sp.edit().putFloat(key, value).commit();
    }

    public static String get(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public static void put(String key, String value) {
        sp.edit().putString(key, value).commit();
    }
}
