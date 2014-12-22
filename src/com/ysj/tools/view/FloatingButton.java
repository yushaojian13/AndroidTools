
package com.ysj.tools.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ysj.tools.R;
import com.ysj.tools.anim.AnimationBundle;
import com.ysj.tools.anim.TargetDrawable;
import com.ysj.tools.anim.Tweener;
import com.ysj.tools.debug.LOG;

public class FloatingButton extends View {

    private int width;
    private int height;

    private Paint paint;
    private RectF rectF;

    private static final int ANIMATION_DURATION = 200;
    private AnimationBundle animationBundle = new AnimationBundle();

    private String line1;
//    private String line2;

    private TargetDrawable arrowDrawable;

    public FloatingButton(Context context) {
        this(context, null);
    }

    public FloatingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        line1 = "摇扬花蕊";
//        line2 = "下载试用";
        arrowDrawable = new TargetDrawable();
        arrowDrawable.setDrawable(getResources(), R.drawable.ic_action_edit_dark);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        setDelta(0);
    }

    private int delta;
    private int rotateDegree;

    public void setDelta(int dlt) {
        delta = dlt;
        rectF = new RectF(0, 0, height + delta, height);
    }

    public void setRotateDegree(int degree) {
        rotateDegree = degree;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawRing(canvas);
        if (delta == width - height) {
            drawText(canvas);
        }
    }

    private void drawRing(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(height / 2, height / 2);
        canvas.rotate(rotateDegree);
        arrowDrawable.draw(canvas);
        canvas.restore();
    }

    private void drawBg(Canvas canvas) {
        paint.setColor(Color.DKGRAY);
        canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
    }

    private void drawText(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(36);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        float tX = height;
        float tY = height / 2 - (paint.descent() - paint.ascent()) * 0.5f;
        canvas.translate(tX, tY);
        canvas.drawText(line1, 0, -paint.ascent() * 1.05f, paint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                rotate();
                LOG.e("");
                break;
        }

        return super.onTouchEvent(event);
    }

    private void rotate() {
        animationBundle.cancel();
        animationBundle.add(Tweener.to(this, ANIMATION_DURATION * 5,
                "ease", new LinearInterpolator(),
                "rotateDegree", new int[] {
                        0, 360 * 3
                },
                "onUpdate", mUpdateListener,
                "onComplete", completeListener
                ));

        animationBundle.start();
    }
    
    private AnimatorListener completeListener = new AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            reset();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
        
    };

    private boolean expanded;

    private void reset() {
        int deltas[] = new int[] {
                0, width - height
        };

        if (expanded) {
            deltas = new int[] {
                    width - height, 0
            };
        }

        expanded = !expanded;

        animationBundle.cancel();
        animationBundle.add(Tweener.to(this, ANIMATION_DURATION,
                "ease", new LinearInterpolator(),
                "delta", deltas,
                "onUpdate", mUpdateListener
                ));

        animationBundle.start();
    }

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }
    };

}
