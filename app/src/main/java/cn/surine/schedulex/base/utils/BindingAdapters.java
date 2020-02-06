package cn.surine.schedulex.base.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.surine.schedulex.base.Constants;

/**
 * Intro：自定义绑定
 * 使用自定义绑定并非一种优雅的方式，尽管它带来很多方便，如果过度依赖绑定这种
 * 东西会造成很难以维护的后果，当然在一定程度上也依赖IDE的支持。
 * <p>
 * BindingAdapter允许添加自定义的绑定，但是能不依靠这个实现就不依靠。
 *
 * @author sunliwei
 * @date 2020-01-17 10:40
 */
public class BindingAdapters {

    /**
     * 给edit text 添加文本监听
     */
    @androidx.databinding.BindingAdapter("textWatcher")
    public static void textWatcher(EditText editText, TextWatcher textWatcher) {
        editText.addTextChangedListener(textWatcher);
    }


    /**
     * 给schedule 卡片标题添加背景
     */
    @androidx.databinding.BindingAdapter("scheduleCardTitleBackground")
    public static void scheduleCardTitleBackground(TextView textView, String color) {
        if (color == null || TextUtils.isEmpty(color)) {
            color = Constants.NORMAL_COLOR;
        }

        textView.setBackground(Drawables.getDrawable(Color.parseColor(color), 180, 10, Color.WHITE));
    }


    /**
     * 给schedule 卡片添加背景
     */
    @androidx.databinding.BindingAdapter("scheduleCardMainBackground")
    public static void scheduleCardMainBackground(LinearLayout linearLayout, String color) {
        if (color == null || TextUtils.isEmpty(color)) {
            color = Constants.NORMAL_COLOR;
        }
        try {
            int[] gradientColor = new int[]{Color.parseColor(color), Color.parseColor("#BEF5F2F2")};
            linearLayout.setBackground(Drawables.getDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColor, 20, 0, Color.parseColor(color)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 控制当前选中课表的一些元素显示与隐藏
     */
    @androidx.databinding.BindingAdapter("ctrlScheduleCardHelperElement")
    public static void ctrlScheduleCardHelperElement(ImageView imageView, int id) {
        long curScheduleId = Prefs.getLong(Constants.CUR_SCHEDULE, -1L);
        if (id == curScheduleId) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }


    /**
     * 课表配置页面色板的颜色显示
     */
    @androidx.databinding.BindingAdapter("schedulePaletteColorUi")
    public static void schedulePaletteColorUi(ImageView imageView, String color) {
        if (color == null || TextUtils.isEmpty(color)) {
            color = Constants.NORMAL_COLOR;
        }
        imageView.setBackground(Drawables.getDrawable(Color.parseColor(color), 180, 0, Color.parseColor(color)));
    }


    /**
     * 给课程添加背景
     */
    @androidx.databinding.BindingAdapter("courseItemCardBackground")
    public static void courseItemCardBackground(LinearLayout linearLayout, String color) {
        if (color != null && !TextUtils.isEmpty(color)) {
            linearLayout.setBackground(Drawables.getDrawable(Color.parseColor(color), 20, 3, Color.WHITE));
        }
    }
}
