package cn.surine.schedulex.base.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/**
 * Intro：创建drawable
 *
 * @author sunliwei
 * @date 2020-01-29 17:24
 */
public class Drawables {

    /**
     * 创建一个常规的shape形状
     *
     * @param color       填充颜色
     * @param corner      圆角
     * @param strokeWidth 描边宽度
     * @param strokeColor 描边颜色
     */
    public static Drawable getDrawable(int color, int corner, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(corner);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }


    /**
     * 创建一个渐变shape形状
     *
     * @param orientation 方向
     * @param colors      颜色值
     * @param strokeColor 描边颜色
     * @param corner      圆角
     * @param strokeWidth 描边宽度
     */
    public static Drawable getDrawable(GradientDrawable.Orientation orientation, int[] colors, float[] corner, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable(orientation, colors);
        gradientDrawable.setCornerRadii(corner);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
