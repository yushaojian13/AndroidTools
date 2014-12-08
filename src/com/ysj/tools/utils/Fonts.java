
package com.ysj.tools.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class Fonts {
    private final static HashMap<String, SoftReference<Typeface>> TYPEFACE_CACHE = new HashMap<String, SoftReference<Typeface>>();

    public static Typeface createTypeface(Context context, String fontPath, boolean cache) {
        Typeface tf = null;
        
        if (fontPath == null) {
        } else if (TYPEFACE_CACHE.containsKey(fontPath)) {
            tf = TYPEFACE_CACHE.get(fontPath).get();
            return tf;
        } else if (fontPath.startsWith("file://")) {
            String trimPath = fontPath.replace("file://", "");
            try {
                tf = Typeface.createFromFile(trimPath);
            } catch (Exception e) {
            }
        } else if (fontPath.startsWith("assets://")) {
            String trimPath = fontPath.replaceFirst("assets://", "");
            try {
                tf = Typeface.createFromAsset(context.getAssets(), trimPath);
            } catch (Exception e) {
            }
        } else {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            } catch (Exception e) {
                try {
                    tf = Typeface.createFromFile(fontPath);
                } catch (Exception e2) {
                }
            }
        }

        if (tf != null && cache) {
            TYPEFACE_CACHE.put(fontPath, new SoftReference<Typeface>(tf));
        }

        return tf;
    }

    public static void setFontToView(TextView textView, String fontPath) {
        setFontToView(textView, fontPath, Typeface.NORMAL);
    }

    public static void setFontToView(TextView textView, String fontPath, int typefaceStyle) {
        Typeface tf = createTypeface(textView.getContext(), fontPath, true);
        textView.setTypeface(tf, typefaceStyle);
    }
}
