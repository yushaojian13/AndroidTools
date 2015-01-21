
package com.ysj.tools.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ysj.tools.R;
import com.ysj.tools.anim.AnimationBundle;
import com.ysj.tools.anim.TargetDrawable;
import com.ysj.tools.anim.Tweener;
import com.ysj.tools.debug.LOG;
import com.ysj.tools.utils.Displays;

public class RandomChangeView extends View {
    private Paint paint;

    private float rotateDegree;
    private TargetDrawable arrowDrawable;

    private float ringWidth;
    private float ringRadius;
    private float ringSpace;
    private boolean rotating;

    private static final int ANIMATION_DURATION = 100;
    private AnimationBundle animationBundle = new AnimationBundle();

    public RandomChangeView(Context context) {
        this(context, null);
    }

    public RandomChangeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        rotating = false;
        ringWidth = Displays.dip2px(getContext(), 1.5f);
        ringSpace = Displays.dip2px(getContext(), 3f);
        arrowDrawable = new TargetDrawable();
        arrowDrawable.setDrawable(getResources(), R.drawable.change);
        LOG.enableLog(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ringRadius = (Math.max(arrowDrawable.getWidth(), arrowDrawable.getHeight()) + ringWidth)
                * 0.5f + ringSpace;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.GRAY);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(ringWidth);
        canvas.drawCircle(getWidth() * 0.5f, getHeight() * 0.5f, ringRadius, paint);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(rotateDegree);
        arrowDrawable.draw(canvas);
        canvas.restore();
    }

    public void setRotateDegree(float degree) {
        rotateDegree = degree;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (!rotating) {
                    rotating = true;
                    rotate();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private void rotate() {
        animationBundle.cancel();
        animationBundle.add(Tweener.to(this, ANIMATION_DURATION * 5,
                "ease", new LinearInterpolator(),
                "rotateDegree", new float[] {
                        0, 360
                },
                "onUpdate", mUpdateListener,
                "onComplete", completeListener
                ));

        animationBundle.start();
    }

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }
    };

    private AnimatorListener completeListener = new AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            rotating = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        
        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            int measuredWidth = (int) Math.ceil((arrowDrawable.getWidth() + (ringWidth + ringSpace) * 2)) + getPaddingLeft()
                    + getPaddingRight();
            int measuredHeight = (int) Math.ceil((arrowDrawable.getHeight() + (ringWidth + ringSpace) * 2)) + getPaddingTop()
                    + getPaddingBottom();
            LOG.v("measured height = " + measuredHeight);
            setMeasuredDimension(measuredWidth, measuredHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    };

}
