
package com.ysj.tools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CampaignViewBak extends ImageView {
    private Paint paint;

    private String line1;
    private String line2;
    private String line3;
    
    private int line1Size = 60;
    private int line2Size = 36;
    private int line3Size = 36;
    
    private Rect rect;
    
    private float ratio = 1.4375f;
    
    private float paddingLeft = 20;
    private float paddingBottom = 20;
    
    public static final int MAX_WORD_NUM = 30;

    public CampaignViewBak(Context context) {
        this(context, null);
    }

    public CampaignViewBak(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CampaignViewBak(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        setText("独家记忆\n它的味道很独特\n至今无法忘记");
        rect = new Rect();
    }

    public void setText(String input) {
        if (TextUtils.isEmpty(input)) {
            return;
        }

        String[] lines = input.split("\n");
        switch (lines.length) {
            case 1:
                line1 = lines[0];
                line2 = null;
                line3 = null;
                break;
            case 2:
                line1 = lines[0];
                line2 = lines[1];
                line3 = null;
                break;
            case 3:
                line1 = lines[0];
                line2 = lines[1];
                line3 = lines[2];
                line3.replace("\n", "");
                break;
        }

        int line1Len = getLen(line1);
        int line2Len = getLen(line2);
        int line3Len = getLen(line3);
        
        if (line1Len > MAX_WORD_NUM) {
            line1 = line1.substring(0, MAX_WORD_NUM);
            line2 = null;
            line3 = null;
        } else if (line1Len + line2Len > MAX_WORD_NUM) {
            line2 = line2.substring(0, MAX_WORD_NUM - line1Len);
            line3 = null;
        } else if (line1Len + line2Len + line3Len > MAX_WORD_NUM) {
            line3 = line3.substring(0, MAX_WORD_NUM - line1Len - line2Len);
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
        
        if (!TextUtils.isEmpty(line1)) {
            sb.append(line1);
        }
        
        if (!TextUtils.isEmpty(line2)) {
            sb.append(line2);
        }
        
        if (!TextUtils.isEmpty(line3)) {
            sb.append(line3);
        }
        
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        paint.setColor(Color.WHITE);
        
        paint.setTextSize(line3Size);
        paint.getTextBounds(line3, 0, line3.length(), rect);
        float tX = -rect.left + paddingLeft;
        float tY = -rect.top + getHeight() - rect.height() - paddingBottom;
        canvas.drawText(line3, tX, tY, paint);
        
        paint.setTextSize(line2Size);
        paint.getTextBounds(line2, 0, line2.length(), rect);
        tY -= rect.height();
        canvas.drawText(line2, tX, tY, paint);
        
        paint.setTextSize(line1Size);
        paint.getTextBounds(line1, 0, line1.length(), rect);
        tY -= rect.height();
        canvas.drawText(line1, tX, tY, paint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(),
                    MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {
            width = (int) (height * ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(),
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    };

}
