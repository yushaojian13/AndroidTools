
package com.ysj.tools.view;

import java.util.ArrayList;
import java.util.List;

import com.ysj.tools.debug.LOG;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;

public class CampaignView1 extends CampaignBaseView {
    public CampaignView1(Context context) {
        this(context, null);
    }

    public CampaignView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CampaignView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setText("希望\n在最深的绝望里\n遇见最美丽的惊喜");
    }
    
    protected void drawText(Canvas canvas) {
        paint.setColor(Color.WHITE);
        float deltaY = 0;
        float space = 0;
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;
        float maxWidth = 0;
        for (int idx = lineList.size() - 1; idx >= 0; idx--) {
            if (idx == 0) {
                paint.setTextSize(bigSize);
                space = bigLineSpace;
                deltaY += bigLineSpace;
            } else {
                paint.setTextSize(smallSize);
                if (idx != lineList.size() - 1) {
                    space = smallSize;
                    deltaY += smallLineSpace;
                }
            }
            String text = lineList.get(idx);
            Rect rect = rectList.get(idx);
            float tX = -rect.left + horizontalPadding;
            float tY = -rect.top + getHeight() - rect.height() - verticalPadding - deltaY;

            List<String> list = new ArrayList<String>();
            while (paint.measureText(text) > getWidth() - horizontalPadding * 2) {
                for (int i = 1; i < text.length(); i++) {
                    String subText = text.substring(0, i);
                    if (paint.measureText(subText) > getWidth() - horizontalPadding * 2) {
                        list.add(text.substring(0, i - 1));
                        text = text.substring(i - 1);
                    }
                }
            }

            if (!TextUtils.isEmpty(text)) {
                list.add(text);
            }

            for (int i = 0; i < list.size(); i++) {
                String str = list.get(list.size() - i - 1);
                tY = tY - (rect.height() + space) * i;
                canvas.drawText(str, tX, tY, paint);
                deltaY += rect.height();
                if (paint.measureText(str) > maxWidth) {
                    maxWidth = paint.measureText(str);
                }
                if (idx == 0 && i == list.size() - 1) {
                    LOG.e(rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom + " "
                            + tY);
                    top = tY + rect.top;
                    right = -rect.left;
                }
                if (idx == lineList.size() - 1 && i == 0) {
                    LOG.e(rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom + " "
                            + tY);
                    bottom = tY + rect.top + rect.height();
                }
            }

        }

        left = horizontalPadding - paddingStroke;
        right += left + maxWidth + paddingStroke * 2;
        top += - paddingStroke;
        bottom += paddingStroke;
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3);
        PathEffect effects = new DashPathEffect(new float[] {
                10, 10
        }, 0);
        paint.setPathEffect(effects);
        canvas.drawRect(left, top, right, bottom, paint);

        editDrawable.setPositionX(right);
        editDrawable.setPositionY(bottom);
        editDrawable.draw(canvas);
    }
}
