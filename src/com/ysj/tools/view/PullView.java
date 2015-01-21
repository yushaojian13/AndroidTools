
package com.ysj.tools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ysj.tools.R;
import com.ysj.tools.anim.TargetDrawable;
import com.ysj.tools.debug.LOG;

public class PullView extends View {
    private TargetDrawable dotDrawable;
    private TargetDrawable ballDrawable;
    private BitmapDrawable lineDrawable;

    private Rect lineRect;

    private Paint paint;

    private float dotX;
    private float dotY;
    private float baseX;
    private float lineLen;
    private float angle;

    private static final float LEFT_MAX_ANGLE = 10;
    private static final float RIGHT_MAX_ANGLE = -LEFT_MAX_ANGLE;

    public PullView(Context context) {
        this(context, null);
    }

    public PullView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);

        dotDrawable = new TargetDrawable();
        ballDrawable = new TargetDrawable();
        lineDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.campaign_line);
        dotDrawable.setDrawable(getResources().getDrawable(R.drawable.campaign_dot));
        ballDrawable.setDrawable(getResources().getDrawable(R.drawable.campaign_ball));

        lineRect = new Rect();

        paint = new Paint();
        paint.setAntiAlias(true);
        
        angle = LEFT_MAX_ANGLE;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        reset();
    }

    private void reset() {
        baseX = getWidth() * 0.9f;
        lineLen = getHeight() * 0.1f;
        dotX = baseX;
        dotY = dotDrawable.getHeight() * 0.5f;
        swing = true;
        handler.sendEmptyMessage(0);
    }

    private float ballX;
    private float ballY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(angle, baseX, 0);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        ballX = baseX;
        ballY = dotDrawable.getWidth() * 0.5f + lineLen + ballDrawable.getHeight() * 0.25f;
        canvas.translate(ballX, ballY);
        ballDrawable.draw(canvas);
        canvas.restore();

        canvas.translate(
                (float) Math.ceil(getWidth() * 0.9 - lineDrawable.getIntrinsicWidth() * 0.35),
                dotDrawable.getWidth() * 0.5f);
        lineRect.set(0, 0, lineDrawable.getIntrinsicWidth(), (int) lineLen);
        lineDrawable.setTileModeXY(TileMode.REPEAT, TileMode.MIRROR);
        lineDrawable.setBounds(lineRect);
        lineDrawable.draw(canvas);
        canvas.restore();

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(dotX, dotY);
        dotDrawable.draw(canvas);
        canvas.restore();
        canvas.restore();
    }

    private float downX;
    private float downY;
    private float moveX;
    private float moveY;
    private boolean move;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                swing = false;
                move = false;
                downX = event.getX();
                downY = event.getY();
                if (Math.sqrt((downX - ballX) * (downX - ballX) + (downY - ballY) * (downY - ballY)) < ballDrawable
                        .getWidth() * 2) {
                    move = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();

                if (Math.sqrt((moveX - ballX) * (moveX - ballX) + (moveY - ballY) * (moveY - ballY)) < ballDrawable
                        .getWidth() * 0.5) {
                    return true;
                }

                if (move) {
                    lineLen = (float) Math.sqrt((moveX - dotX) * (moveX - dotX) + (moveY - dotY)
                            * (moveY - dotY));
                    angle = (float) Math.toDegrees(Math.atan2(dotX - moveX, moveY - dotY));
                    LOG.e(angle);
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                reset();
                invalidate();
                if (releaseListener != null) {
                    releaseListener.onRelease();
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    private boolean swing;
    private boolean leftToRight;
    
    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (!swing) {
                return;
            }
            
            if (angle - RIGHT_MAX_ANGLE < -0.000001) {
                leftToRight = false;
            } else if(angle - LEFT_MAX_ANGLE > 0.000001) {
                leftToRight = true;
            }
            
            if (leftToRight) {
                angle = angle - 0.1f;
            } else {
                angle = angle + 0.1f;
            }

            invalidate();
            handler.sendEmptyMessageDelayed(0, 1);
        };
    };

    private OnReleaseListener releaseListener;

    public void setOnReleaseListener(OnReleaseListener listener) {
        this.releaseListener = listener;
    }

    public interface OnReleaseListener {
        public void onRelease();
    }
}
