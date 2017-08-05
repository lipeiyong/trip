package net.lvtushiguang.trip.util;

import android.content.Context;

/**
 * 尺寸转换帮助类
 * <p>
 * Created by 薰衣草 on 2016/8/9.
 */
public class SizeUtils {
    /**
     * dp转换为px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
