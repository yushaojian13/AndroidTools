
package com.ysj.tools.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ysj.tools.R;
import com.ysj.tools.anim.AnimationBundle;
import com.ysj.tools.anim.TargetDrawable;
import com.ysj.tools.anim.Tweener;

public class PullView extends View {
    private int lineColor;
    private TargetDrawable ballDrawable;

    private float lineMinLen = 150;

    private float ballRadius;

    private float lineStartX;
    private float lineStartY;
    private float lineEndX;
    private float lineEndY;
    private float leftAngle;
    private float rightAngle;

    private Paint paint;

    private static final int ANIMATION_DURATION = 1000;
    private AnimationBundle animationBundle = new AnimationBundle();

    private static final int LEFT_TO_RIGHT = 0;
    private static final int RIGHT_TO_LEFT = 1;

    public PullView(Context context) {
        super(context);
        init(context);
    }

    public PullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ballDrawable = new TargetDrawable();

        setBall(getResources().getDrawable(R.drawable.ball));
        setLineColor(getResources().getColor(R.color.orangetext));

        leftAngle = (float) (-Math.PI / 36);
        rightAngle = (float) (Math.PI / 36);

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setLineColor(int color) {
        this.lineColor = color;
        invalidate();
    }

    public void setBall(Drawable drawable) {
        ballDrawable.setDrawable(drawable);
        ballRadius = Math.max(ballDrawable.getWidth(), ballDrawable.getHeight()) * 0.5f;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        lineStartX = getWidth() * 0.9f;
        lineStartY = Math.min(getHeight() * 0.1f, 20);
        setAngle(leftAngle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawBall(canvas);
    }

    private void drawLine(Canvas canvas) {
        paint.setColor(lineColor);
        canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, paint);
    }

    private void drawBall(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(lineEndX, lineEndY);
        ballDrawable.draw(canvas);
        canvas.restore();
    }

    private int round = 0;
    private boolean swing = true;

    public void setAngle(float angle) {
        lineEndX = (float) (lineStartX + lineMinLen * Math.sin(angle));
        lineEndY = (float) (lineStartY + lineMinLen * Math.cos(angle));

        if (swing && (round % 2 == 0) && (Math.abs(angle - leftAngle) < 0.000001)) {
            handler.sendEmptyMessage(LEFT_TO_RIGHT);
            round++;
            round = round % 2;
        } else if (swing && (round % 2 == 1) && (Math.abs(angle - rightAngle) < 0.000001)) {
            handler.sendEmptyMessage(RIGHT_TO_LEFT);
            round++;
            round = round % 2;
        }

    }

    private boolean go = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                swing = false;
                animationBundle.cancel();
                float downX = event.getX();
                float downY = event.getY();
                float deltaX = downX - lineEndX;
                float deltaY = downY - lineEndY;
                if (deltaX * deltaX + deltaY * deltaY < ballRadius * ballRadius * 4) {
                    go = true;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (go) {
                    lineEndX = event.getX();
                    lineEndY = event.getY();
                    invalidate();
                    return true;
                }
            case MotionEvent.ACTION_UP:
                if (releaseListener != null) {
                    releaseListener.onRelease();
                }
                swing = true;
                setAngle(leftAngle);
                return true;
        }

        return false;
    }

    private void go(float from, float to) {
        go = false;
        animationBundle.cancel();
        animationBundle.add(Tweener.to(this, ANIMATION_DURATION,
                "ease", new LinearInterpolator(),
                "angle", new float[] {
                        from, to
                },
                "onUpdate", mUpdateListener
                ));

        animationBundle.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LEFT_TO_RIGHT:
                    go(leftAngle, rightAngle);
                    break;
                case RIGHT_TO_LEFT:
                    go(rightAngle, leftAngle);
                    break;
            }
        };
    };

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }
    };
    
    private OnReleaseListener releaseListener;
    
    public void setOnReleaseListener(OnReleaseListener listener) {
        this.releaseListener = listener;
    }
    
    public interface OnReleaseListener {
        public void onRelease();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
