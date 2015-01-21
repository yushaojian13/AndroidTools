
package com.ysj.tools.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ysj.tools.R;
import com.ysj.tools.anim.TargetDrawable;

public abstract class CampaignBaseView extends ImageView {
    protected List<String> lineList;
    protected List<Rect> rectList;
    protected TargetDrawable editDrawable;

    protected  float bigSize = 80;
    protected  float smallSize = 36;
    protected  float bigLineSpace = 10;
    protected  float smallLineSpace = 5;
    protected  float bigTextSpace = 3;
    protected  float smallTextSpace = 1;
    protected  float horizontalPadding = 50;
    protected  float verticalPadding = 50;
    protected  float paddingStroke = 5;
    protected  int MAX_LINE_NUM = 3;
    protected  int MAX_WORD_NUM = 20;
    protected  float ratio = 1.4375f;

    protected Paint paint;

    public CampaignBaseView(Context context) {
        this(context, null);
    }

    public CampaignBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CampaignBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        lineList = new ArrayList<String>();
        rectList = new ArrayList<Rect>();
        editDrawable = new TargetDrawable();
        editDrawable.setDrawable(getResources(), R.drawable.campaign_edit);
    }

    public void setText(String input) {
        if (TextUtils.isEmpty(input)) {
            return;
        }

        lineList.clear();
        rectList.clear();

        String[] lines = input.split("\n");
        if (lines.length > MAX_LINE_NUM - 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = MAX_LINE_NUM - 1; i < lines.length; i++) {
                sb.append(lines[i]);
            }
            lineList.add(lines[0]);
            lineList.add(lines[1]);
            lineList.add(sb.toString());
        } else {
            lineList.addAll(Arrays.asList(lines));
        }

        int lenSum = 0;
        int pos = 0;
        boolean flag = false;
        Iterator<String> iterator = lineList.iterator();
        while (iterator.hasNext()) {
            String text = iterator.next();
            if (flag) {
                iterator.remove();
                continue;
            }

            int len = getLen(text);
            if (lenSum + len > MAX_WORD_NUM) {
                text = text.substring(0, MAX_WORD_NUM - lenSum);
                lineList.set(pos, text);
                flag = true;
            }

            pos++;
            lenSum += len;
        }

        for (int idx = 0; idx < lineList.size(); idx++) {
            float textSize;
            if (idx == 0) {
                textSize = bigSize;
            } else {
                textSize = smallSize;
            }

            String text = lineList.get(idx);
            paint.setTextSize(textSize);
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            rectList.add(rect);
        }
    }

    private int getLen(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }

        return str.length();
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }
    
    protected abstract void drawText(Canvas canvas);

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop()
                    + getPaddingBottom(),
                    MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY
                && ratio != 0.0f) {
            width = (int) (height * ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft()
                    + getPaddingRight(),
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    };

}
