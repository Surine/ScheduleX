package cn.surine.schedulex.base.utils;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;

import java.util.HashMap;

/**
 * Intro：
 * 取色
 *
 * @author sunliwei
 * @date 2020-01-29 17:30
 */
public class Palettes {

    /**
     * 取一张图片中的色调
     *
     * @param bitmap 图片
     * @return 色彩列表
     */
    public HashMap<String, Palette.Swatch> getColors(@NonNull Bitmap bitmap) {
        HashMap<String, Palette.Swatch> map = new HashMap<>(7);
        Palette.from(bitmap).generate(palette -> {
            if (palette != null) {
                Palette.Swatch s = palette.getDominantSwatch();//独特的一种
                Palette.Swatch s1 = palette.getVibrantSwatch();       //获取到充满活力的这种色调
                Palette.Swatch s2 = palette.getDarkVibrantSwatch();    //获取充满活力的黑
                Palette.Swatch s3 = palette.getLightVibrantSwatch();   //获取充满活力的亮
                Palette.Swatch s4 = palette.getMutedSwatch();           //获取柔和的色调
                Palette.Swatch s5 = palette.getDarkMutedSwatch();      //获取柔和的黑
                Palette.Swatch s6 = palette.getLightMutedSwatch();    //获取柔和的亮
                map.put("Dominant", s);
                map.put("Vibrant", s1);
                map.put("DarkVibrant", s2);
                map.put("LightVibrant", s3);
                map.put("Muted", s4);
                map.put("DarkMuted", s5);
                map.put("LightMuted", s6);
            }
        });
        return map;
    }
}
