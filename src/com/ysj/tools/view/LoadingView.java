
package com.ysj.tools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ysj.tools.R;
import com.ysj.tools.utils.Maths;

public class LoadingView extends View {
    private float dotRadius;
    private int dotCount;
    private int headColor;
    private int tailColor;

    private static final int DOT_RADIUS = 4;
    private static final int DOT_COUNT = 12;
    private static final int HEAD_COLOR = 0xff333333;
    private static final int TAIL_COLOR = 0xffcccccc;

    private float centerX;
    private float centerY;
    private float circleRadius;

    private Paint paint;

    public LoadingView(Context context) {
        super(context);
        init(context);
        setDefault(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        resolveAttrs(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        resolveAttrs(context, attrs);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    private void setDefault(Context context) {
        dotRadius = DOT_RADIUS;
        dotCount = DOT_COUNT;
        headColor = HEAD_COLOR;
        tailColor = TAIL_COLOR;
    }

    private void resolveAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        dotRadius = a.getDimensionPixelSize(R.styleable.LoadingView_dotRadius, DOT_RADIUS);
        dotCount = a.getDimensionPixelSize(R.styleable.LoadingView_dotCount, DOT_COUNT);
        headColor = a.getColor(R.styleable.LoadingView_headColor, HEAD_COLOR);
        tailColor = a.getColor(R.styleable.LoadingView_tailColor, TAIL_COLOR);
        a.recycle();
    }

    public void setColor(int outer, int inner) {
        headColor = outer;
        tailColor = inner;
    }

    public void setRadius(int rx, int rw) {
        dotRadius = rx;
        dotCount = rw;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = getWidth() * 0.5f;
        centerY = getHeight() * 0.5f;
        circleRadius = Math.min(centerX, centerY) - dotRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int alpha;
        int red;
        int green;
        int blue;
        int color;
        double angle;
        float cx;
        float cy;
        for (int i = 0; i < dotCount; i++) {
            angle = Math.PI / dotCount * 2 * (i - start);
            cx = (float) (getWidth() * 0.5 + circleRadius * Math.cos(angle));
            cy = (float) (getHeight() * 0.5 - circleRadius * Math.sin(angle));
            alpha = (int) Maths.computeY(0, 12, Color.alpha(headColor), Color.alpha(tailColor), i);
            red = (int) Maths.computeY(0, 12, Color.red(headColor), Color.red(tailColor), i);
            green = (int) Maths.computeY(0, 12, Color.green(headColor), Color.green(tailColor), i);
            blue = (int) Maths.computeY(0, 12, Color.blue(headColor), Color.blue(tailColor), i);
            color = Color.argb(alpha, red, green, blue);
            paint.setColor(color);
            canvas.drawCircle(cx, cy, dotRadius, paint);
        }
    }

    private int start = 0;

    public void setStart(int offset) {
        start = offset;
    }

    private boolean flag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                flag = !flag;
                if (flag) {
                    update = true;
                    handler.sendEmptyMessage(0);
                } else {
                    update = false;
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private boolean update = true;
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (update) {
                        start = (start + 1) % dotCount;
                        invalidate();
                        handler.sendEmptyMessageDelayed(0, 50);
                    }
                    break;
            }
        };
    };
}
