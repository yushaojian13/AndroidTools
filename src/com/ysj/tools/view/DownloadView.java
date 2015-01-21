
package com.ysj.tools.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.ysj.tools.R;
import com.ysj.tools.anim.TargetDrawable;

public class DownloadView extends View {
    private float outerArc = 2;
    private float innerArc = 5;

    private float outerCircleRadius;
    private float innerCircleRadius;

    private RectF outerRectF;
    private RectF innerRectF;

    private Paint paint;

    private TargetDrawable arrowDrawable;

    private State state;
    private int percent;

    private enum State {
        NORMAL, DOWNLOADING, PAUSE, DOWNLOADED
    }

    public DownloadView(Context context) {
        this(context, null);
    }

    public DownloadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);

        paint = new Paint();
        paint.setAntiAlias(true);

        outerRectF = new RectF();
        innerRectF = new RectF();

        arrowDrawable = new TargetDrawable();

        state = State.NORMAL;
        arrowDrawable.setDrawable(getResources(), R.drawable.ic_action_edit_light);
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
        canvas.drawArc(innerRectF, 270, (float) percent / 100 * 360, false, paint);

        drawRing(canvas);
    }

    private void drawRing(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(getWidth() * 0.5f, getHeight() * 0.5f);
        arrowDrawable.draw(canvas);
        canvas.restore();
    }

    public void setProgress(int percent) {
        this.percent = percent;

        if (state == State.DOWNLOADING) {
            invalidate();
        }
    }

    @Override
    public boolean performClick() {
        if (state == State.NORMAL || state == State.PAUSE) {
            state = State.DOWNLOADING;
            arrowDrawable.setDrawable(getResources(), R.drawable.ic_action_edit_dark);
            handler.sendEmptyMessage(0);
        } else if (state == State.DOWNLOADING) {
            state = State.PAUSE;
            arrowDrawable.setDrawable(getResources(), R.drawable.ic_action_edit_light);
        }

        return super.performClick();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            percent++;
            if (percent > 100) {
                // angle = 0;
                state = State.DOWNLOADED;
                arrowDrawable.setDrawable(getResources(), R.drawable.ball);
                invalidate();
                return;
            }

            invalidate();

            if (state == State.DOWNLOADING) {
                handler.sendEmptyMessageDelayed(0, 10);
            }
        };
    };
}
