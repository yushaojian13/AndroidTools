package com.ysj.tools.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ysj.tools.utils.Displays;
import com.ysj.tools.utils.Fonts;

public class ImageCreator {
    private String text;
    private float textSize;
    private int textColor;
    private String fontPath;

    private Rect textRect;
    private Paint paint;
    private Context context;

    public ImageCreator(Context context, String text, float textSize, int textColor, String fontPath) {
        this.context = context;
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.fontPath = fontPath;

        paint = new Paint();
        paint.setAntiAlias(true);
        textRect = new Rect();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextSize(float size) {
        this.textSize = Displays.dip2px(context, textSize);
    }
    
    public void setTextColor(int color) {
        this.textColor = color;
    }

    public void setFontPath(String path) {
        this.fontPath = path;
    }

    private void computeRect() {
        paint.setTextSize(textSize);
        Typeface typeface = Fonts.createTypeface(context, fontPath, false);
        if (typeface != null) {
            paint.setTypeface(typeface);
        }
        paint.getTextBounds(text, 0, text.length(), textRect);
    }

    public Drawable createDrawable() {
        if (text == null || text.length() == 0) {
            return null;
        }
        
        computeRect();
        
        int bitmapWidth = textRect.width();
        int bitmapHeight = textRect.height();

        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        paint.setColor(textColor);
        canvas.drawText(text, -textRect.left, -textRect.top, paint);

        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }
}
