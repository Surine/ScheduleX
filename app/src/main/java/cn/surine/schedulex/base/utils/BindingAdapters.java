package cn.surine.schedulex.base.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.data.entity.Event;

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


//    @androidx.databinding.BindingAdapter("bindChildTask")
//    public static void bindChildTask(ViewGroup viewGroup, List<Event> tasks) {
//        for (int i = 0; i < tasks.size(); i++) {
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_child_event_display, null);
//            CheckBox checkBox = view.findViewById(R.id.taskInfo);
//            checkBox.setChecked(tasks.get(i).getDone());
//            checkBox.setText(tasks.get(i).getContent());
//            viewGroup.addView(view);
//        }
//    }


    /**
     * 控制当前选中课表的一些元素显示与隐藏
     */
    @androidx.databinding.BindingAdapter("ctrlScheduleCardHelperElement")
    public static void ctrlScheduleCardHelperElement(View view, int id) {
        long curScheduleId = Prefs.getLong(Constants.CUR_SCHEDULE, -1L);
        if (id == curScheduleId) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    /**
     * 给image设置背景（使用glide）
     */
    @androidx.databinding.BindingAdapter("glideToImage")
    public static void glideToImage(ImageView imageView, String s) {
        if (s != null) {
            Glide.with(App.context).load(new File(s)).into(imageView);
        }
    }

    /**
     * 给image设置背景（使用glide）
     */
    @androidx.databinding.BindingAdapter("glideToImageUrl")
    public static void glideToImageUrl(ImageView imageView, String s) {
        if (s != null) {
            Glide.with(App.context).load(s).into(imageView);
        }
    }


    /**
     * 给view设置背景
     */
    @androidx.databinding.BindingAdapter(value = {"baseColor", "baseAlpha"})
    public static void setViewDrawable(View view, String baseColor, float baseAlpha) {
        if (baseColor == null || TextUtils.isEmpty(baseColor)) {
            baseColor = Constants.NORMAL_COLOR;
        }
        if (baseAlpha == 0) baseAlpha = 1;
        int colorInt = Uis.getColorWithAlpha(baseAlpha, Color.parseColor(baseColor));
        view.setBackground(Drawables.getDrawable(colorInt, 20, 0, Color.WHITE));
    }

    /**
     * 返回星期字符串
     */
    @SuppressLint("SetTextI18n")
    @androidx.databinding.BindingAdapter("setWeekTextByDayId")
    public static void setWeekTextByDayId(View view, String dayId) {
        if (view instanceof TextView) {
            ((TextView) view).setText("周" + Dates.getWeekInChi(Integer.parseInt(dayId)));
        }
    }

    @androidx.databinding.BindingAdapter("loadPaletteDrawable")
    public static void loadPaletteDrawable(ImageView view, String[] data) {
        int[] colors = new int[Math.min(2, data.length)];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Color.parseColor(data[i]);
        }
        float[] radius = new float[]{
                40, 40, 0, 0, 0, 0, 40, 40
        };
        Drawable curDrawable = Drawables.getDrawable(GradientDrawable.Orientation.TL_BR,
                colors,
                radius,
                0, Color.TRANSPARENT
        );
        view.setImageDrawable(curDrawable);
    }

}
