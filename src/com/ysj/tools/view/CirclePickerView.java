
package com.ysj.tools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.ysj.tools.debug.LOG;
import com.ysj.tools.utils.Displays;

public class CirclePickerView extends View {
    private float circleCenterX;
    private float circleCenterY;
    private float circleRadius;

    private double dotAngle;
    private float dotCenterX;
    private float dotCenterY;
    private float dotRadius;

    private int max;
    private String topText;
    private String rightText;
    private String bottomText;
    private String leftText;
    private String centerText;
    private float textSize;

    private Rect topRct;
    private Rect rightRect;
    private Rect bottomRect;
    private Rect leftRect;
    private Rect centerRect;

    private int slop;
    private Paint paint;

    private CenterChangeMode centerChangeMode;

    public enum CenterChangeMode {
        NONE, NODE_VALUE, REAL_TIME
    }

    public CirclePickerView(Context context) {
        this(context, null);
    }

    public CirclePickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LOG.enableLog(true);
        setClickable(true);
        paint = new Paint();
        paint.setAntiAlias(true);

        topRct = new Rect();
        rightRect = new Rect();
        bottomRect = new Rect();
        leftRect = new Rect();
        centerRect = new Rect();

        max = 4;

        textSize = Displays.dip2px(getContext(), 12);
        paint.setTextSize(textSize);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        slop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        setLeftText("左上");
        setTopText("右上");
        setRightText("左下");
        setBottomText("右下");
        setCenterText("Mode");
        
        setCenterChangeMode(CenterChangeMode.REAL_TIME);
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
        paint.getTextBounds(leftText, 0, leftText.length(), leftRect);
        invalidate();
    }

    public void setTopText(String topText) {
        this.topText = topText;
        paint.getTextBounds(topText, 0, topText.length(), topRct);
        invalidate();
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        paint.getTextBounds(rightText, 0, rightText.length(), rightRect);
        invalidate();
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        paint.getTextBounds(bottomText, 0, bottomText.length(), bottomRect);
        invalidate();
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
        paint.getTextBounds(centerText, 0, centerText.length(), centerRect);
        invalidate();
    }

    public void setCenterChangeMode(CenterChangeMode centerChangeMode) {
        this.centerChangeMode = centerChangeMode;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        dotRadius = Math.min(getWidth(), getHeight()) * 0.02f;

        circleCenterX = getWidth() * 0.5f;
        circleCenterY = getHeight() * 0.5f;
        float maxWidth = Math.max(Math.max(leftRect.width(), topRct.width()),
                Math.max(rightRect.width(), bottomRect.width()));
        float maxHeght = Math.max(Math.max(leftRect.height(), topRct.height()),
                Math.max(rightRect.height(), bottomRect.height()));
        float space = Math.max(maxWidth, maxHeght);

        circleRadius = Math.min(getWidth(), getHeight()) * 0.5f - dotRadius - topRct.height()
                - space;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        drawCircle(canvas);
        drawDot(canvas);
        drawTop(canvas);
        drawRight(canvas);
        drawBottom(canvas);
        drawLeft(canvas);
        drawCenter(canvas);
    }

    private void drawCircle(Canvas canvas) {
        paint.setColor(Color.BLUE);
        paint.setStyle(Style.STROKE);
        canvas.drawCircle(circleCenterX, circleCenterY, circleRadius, paint);
    }

    private void drawDot(Canvas canvas) {
        dotCenterX = (float) (circleCenterX + circleRadius * Math.sin(dotAngle));
        dotCenterY = (float) (circleCenterY - circleRadius * Math.cos(dotAngle));
        paint.setStyle(Style.FILL);
        canvas.drawCircle(dotCenterX, dotCenterY, dotRadius, paint);
    }

    private void drawTop(Canvas canvas) {
        float tx = -topRct.left + (getWidth() - topRct.width()) * 0.5f;
        float ty = -topRct.top + ((getHeight() - circleRadius * 2) * 0.5f - topRct.height()) * 0.5f;
        canvas.drawText(topText, tx, ty, paint);
    }

    private void drawRight(Canvas canvas) {
        float tx = -rightRect.left - ((getWidth() - circleRadius * 2) * 0.5f - rightRect.width())
                * 0.5f + getWidth() - rightRect.width();
        float ty = -rightRect.top + (getHeight() - rightRect.height()) * 0.5f;
        canvas.drawText(rightText, tx, ty, paint);
    }

    private void drawBottom(Canvas canvas) {
        float tx = -bottomRect.left + (getWidth() - bottomRect.width()) * 0.5f;
        float ty = -bottomRect.top
                + ((getHeight() - circleRadius * 2) * 0.5f - bottomRect.height()) * 0.5f
                + (getHeight() - circleRadius * 2) * 0.5f + circleRadius * 2;
        canvas.drawText(bottomText, tx, ty, paint);
    }

    private void drawLeft(Canvas canvas) {
        float tx = -leftRect.left + ((getWidth() - circleRadius * 2) * 0.5f - leftRect.width())
                * 0.5f;
        float ty = -leftRect.top + (getHeight() - leftRect.height()) * 0.5f;
        canvas.drawText(leftText, tx, ty, paint);
    }

    private void drawCenter(Canvas canvas) {
        float tx = -centerRect.left + (getWidth() - centerRect.width()) * 0.5f;
        float ty = -centerRect.top + (getHeight() - centerRect.height()) * 0.5f;
        switch (centerChangeMode) {
            case NODE_VALUE:
                if (dotAngle > Math.PI * 1.5) {
                    setCenterText(leftText);
                } else if (dotAngle > Math.PI * 1) {
                    setCenterText(bottomText);
                } else if (dotAngle > Math.PI * 0.5) {
                    setCenterText(rightText);
                } else {
                    setCenterText(topText);
                }
                break;
            case REAL_TIME:
                if (max != 0) {
                    setCenterText(String.valueOf((int) (dotAngle / (Math.PI / 2))));
                }
                break;
            case NONE:
                break;
        }
        canvas.drawText(centerText, tx, ty, paint);
    }

    private boolean invalid;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                double delta = Math.sqrt((downX - dotCenterX) * (downX - dotCenterX)
                        + (downY - dotCenterY)
                        * (downY - dotCenterY));
                if (delta < slop) {
                    invalid = true;
                    return true;
                } else {
                    invalid = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (invalid) {
                    float moveX = event.getX();
                    float moveY = event.getY();
                    dotAngle = Math.atan2(moveX - circleCenterX, circleCenterY - moveY);
                    if (dotAngle < 0) {
                        dotAngle += Math.PI * 2;
                    }
                    invalidate();
                    return true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}
