
package com.ysj.tools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;

public class CampaignView2 extends CampaignBaseView {
    public CampaignView2(Context context) {
        this(context, null);
    }

    public CampaignView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CampaignView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bigLineSpace = 15;
        smallLineSpace = 15;
        smallSize = 45;
        setText("希望\n在最深的绝望里\n遇见最美丽的惊喜");
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
}
