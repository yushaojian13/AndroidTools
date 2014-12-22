
package com.ysj.tools.utils;

public class Maths {
    public static float computeY(float startX, float endX, float startY, float endY, float x) {
        if (x <= startX) {
            return startY;
        } else if (x >= endX) {
            return endY;
        }

        float k = (endY - startY) / (endX - startX);
        float y = startY + (x - startX) * k;
        return y;
    }
    
    public static int getIndex(int[] array, int value) {
        int index = -1;
        
        for (int i : array) {
            if (value == array[i]) {
                index = i;
                break;
            }
        }
        
        return index;
    }
}
