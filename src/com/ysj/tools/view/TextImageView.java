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

public class TextImageView extends ImageView {
    private List<String> lineList;
    private List<Rect> rectList;
    private TargetDrawable editDrawable;

    private  float bigSize = 80;
    private  float smallSize = 36;
    private  float bigLineSpace = 10;
    private  float smallLineSpace = 5;
    private  float bigTextSpace = 3;
    private  float smallTextSpace = 1;
    private  float horizontalPadding = 50;
    private  float verticalPadding = 50;
    private  float paddingStroke = 5;
    private  int lineNum = 3;
    private  int textNum = 20;
    private  float ratio = 1.4375f;

    private Paint paint;
    
    public static enum Mode {
        LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM
    }
    
    public static enum Orientation {
        HORIZONTAL, VERTICAL
    }

    public TextImageView(Context context) {
        this(context, null);
    }

    public TextImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        lineList = new ArrayList<String>();
        rectList = new ArrayList<Rect>();
        editDrawable = new TargetDrawable();
        editDrawable.setDrawable(getResources(), R.drawable.campaign_edit);
        
        bigLineSpace = 15;
        smallLineSpace = 15;
        smallSize = 45;
        setText("希望\n在最深的绝望里\n遇见最美丽的惊喜");
    }

    public void setText(String input) {
        if (TextUtils.isEmpty(input)) {
            return;
        }

        lineList.clear();
        rectList.clear();

        String[] lines = input.split("\n");
        if (lines.length > lineNum - 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = lineNum - 1; i < lines.length; i++) {
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
            if (lenSum + len > textNum) {
                text = text.substring(0, textNum - lenSum);
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
    
    protected void drawText(Canvas canvas) {
        paint.setColor(0xff4b115d);
        float deltaX = horizontalPadding;
        float textSpace = 0;
        int listLen = lineList.size();
        for (int i = 0; i < listLen; i++) {
            int idx = listLen - 1 -i;
            String text = lineList.get(idx);
            Rect rect = rectList.get(idx);
            float deltaY = verticalPadding;
            
            if (idx == 0) {
                paint.setTextSize(bigSize);
                deltaX += bigLineSpace ;
                textSpace = bigTextSpace;
            } else {
                paint.setTextSize(smallSize);
                textSpace = smallTextSpace;
                if (idx != lineList.size() - 1) {
                    deltaX += smallLineSpace;
                }
            }
            
            int len = text.length();
            float maxWidth = 0;
            for (int j = 0; j < len; j++) {
                String sub = text.substring(j, j+1);
                float tX = -rect.left + deltaX;
                float tY = -rect.top + deltaY;
                canvas.drawText(sub, tX, tY, paint);
                float subWidth = paint.measureText(sub);
                if (subWidth > maxWidth) {
                    maxWidth = subWidth;
                }
                deltaY += rect.height() + textSpace;
            }
            deltaX += maxWidth;
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
    }

}
