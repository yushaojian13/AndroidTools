
package com.ysj.tools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class DownloadView extends View {
    private float outerArc = 2;
    private float innerArc = 5;

    private float outerCircleRadius;
    private float innerCircleRadius;

    private RectF outerRectF;
    private RectF innerRectF;

    private Paint paint;

    public DownloadView(Context context) {
        this(context, null);
    }

    public DownloadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        paint.setAntiAlias(true);

        outerRectF = new RectF();
        innerRectF = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        outerCircleRadius = Math.min(getWidth(), getHeight() - outerArc) * 0.5f - 1;
        innerCircleRadius = Math.min(getWidth(), getHeight() - innerArc) * 0.5f - outerArc - 1;

        float outerLeft = getWidth() * 0.5f - outerCircleRadius;
        float outerTop = getHeight() * 0.5f - outerCircleRadius;
        float outerRight = getWidth() * 0.5f + outerCircleRadius;
        float outerBottom = getHeight() * 0.5f + outerCircleRadius;
        outerRectF.set(outerLeft, outerTop, outerRight, outerBottom);

        float innerLeft = getWidth() * 0.5f - innerCircleRadius;
        float innerTop = getHeight() * 0.5f - innerCircleRadius;
        float innerRight = getWidth() * 0.5f + innerCircleRadius;
        float innerBottom = getHeight() * 0.5f + innerCircleRadius;
        innerRectF.set(innerLeft, innerTop, innerRight, innerBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.BLUE);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(outerArc);
        canvas.drawArc(outerRectF, 0, 360, false, paint);

        paint.setColor(Color.RED);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(innerArc);
        canvas.drawArc(innerRectF, 270, angle, false, paint);
    }
    
    private int angle;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            invalidate();
            angle++;
            if (angle > 360) {
                angle = 0;
            }
            handler.sendEmptyMessageDelayed(0, 10);
        };
    };
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.sendEmptyMessage(0);
    }
}
