
package com.ysj.tools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ysj.tools.R;
import com.ysj.tools.utils.Displays;
import com.ysj.tools.utils.Fonts;

public class RoundView extends View {
    private float cornerWidth;
    private float ringWidth;
    private int outerColor;
    private int innerColor;

    private RectF outerRectF;
    private RectF innerRectF;
    private Rect textRect;

    private static final float CORNER_WIDTH = 5;
    private static final float RING_WIDTH = 1;
    private static final int OUTER_COLOR = 0xff000000;
    private static final int INNER_COLOR = 0xffffffff;

    private String text;

    private Paint paint;

    public RoundView(Context context) {
        super(context);
        init(context);
        setDefault(context);
    }

    public RoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        resolveAttrs(context, attrs);
    }

    public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        resolveAttrs(context, attrs);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        outerRectF = new RectF();
        innerRectF = new RectF();
        textRect = new Rect();

        paint.setTypeface(Fonts.createTypeface(context, "fonts/Helvetica-Light.ttf", false));
        paint.setTextSize(16 * Displays.getDensity(context));
        text = "01:23";
        paint.getTextBounds(text, 0, text.length(), textRect);
    }

    private void setDefault(Context context) {
        cornerWidth = (int) (CORNER_WIDTH * Displays.getDensity(context));
        ringWidth = (int) (RING_WIDTH * Displays.getDensity(context));
        outerColor = OUTER_COLOR;
        innerColor = INNER_COLOR;
    }

    private void resolveAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundView);
        cornerWidth = a.getDimensionPixelSize(R.styleable.RoundView_cornerSize,
                (int) (CORNER_WIDTH * Displays.getDensity(context)));
        ringWidth = a.getDimensionPixelSize(R.styleable.RoundView_ringSize,
                (int) (RING_WIDTH * Displays.getDensity(context)));
        outerColor = a.getColor(R.styleable.RoundView_outerColor, OUTER_COLOR);
        innerColor = a.getColor(R.styleable.RoundView_innerColor, INNER_COLOR);
        a.recycle();
    }

    public void setColor(int outer, int inner) {
        outerColor = outer;
        innerColor = inner;
    }

    public void setWidth(int rx, int rw) {
        cornerWidth = rx;
        ringWidth = rw;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        outerRectF.set(0, 0, getWidth(), getHeight());
        innerRectF.set(ringWidth, ringWidth, getWidth() - ringWidth, getHeight() - ringWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(outerColor);
        canvas.drawRoundRect(outerRectF, cornerWidth, cornerWidth, paint);

        if (outerColor != innerColor && ringWidth != 0) {
            paint.setColor(innerColor);
            canvas.drawRoundRect(innerRectF, cornerWidth - ringWidth, cornerWidth - ringWidth,
                    paint);
        }

        paint.setColor(outerColor);
        float textX = -textRect.left + (getWidth() - textRect.width()) * 0.5f;
        float textY = -textRect.top + (getHeight() - textRect.height()) * 0.5f;
        canvas.drawText(text, textX, textY, paint);
    }

}
