package com.digger.coupon.util;

import android.content.Context;

/**
 * Author: DiggerZzz
 * Date: 2018/7/9 14:22
 * Desc:
 */
public class ScreenTool {


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
