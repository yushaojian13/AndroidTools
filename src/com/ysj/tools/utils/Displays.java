package com.ysj.tools.utils;

import android.content.Context;

public class Displays {
    public static int getDW(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    
    public static int getDH(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
    
    public static float getScale(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }
    
    public static float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }
    
    public static float dip2px(Context context, float dp) {
        return dp * getDensity(context);
    }
    
    public static float px2dip(Context context, float px) {
        return px / getDensity(context);
    }
    
    public static float sp2px(Context context, float dp) {
        return dp * getScale(context);
    }
    
    public static float px2sp(Context context, float px) {
        return px / getScale(context);
    }
}
