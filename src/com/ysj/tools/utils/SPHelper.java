
package com.ysj.tools.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {
    private  SharedPreferences sp;
    
    private static volatile SPHelper defaultSpHelper;
    
    public SPHelper(Context context, String tag) {
        sp = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
    }
    
    public static SPHelper getDefault(Context context) {
        if (defaultSpHelper == null) {
            synchronized (SPHelper.class) {
                if (defaultSpHelper == null) {
                    defaultSpHelper = new SPHelper(context, "sp_defautl_tag");
                }
            }
        }
        
        return defaultSpHelper;
    }

    public  boolean get(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public  void put(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    public  int get(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public  void put(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public  float get(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public  void put(String key, float value) {
        sp.edit().putFloat(key, value).commit();
    }

    public  String get(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public  void put(String key, String value) {
        sp.edit().putString(key, value).commit();
    }
}
