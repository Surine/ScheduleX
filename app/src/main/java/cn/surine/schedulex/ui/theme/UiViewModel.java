package cn.surine.schedulex.ui.theme;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.ViewModel;

import cn.surine.schedulex.base.utils.Drawables;

/**
 * Intro：控制UI用的viewmodel
 * @author sunliwei
 * @date 2020-01-29 18:16
 */
public class UiViewModel extends ViewModel {

    /**
     * 获取课表卡片标题背景
     * */
    public Drawable getScheduleCardTitleBackground(int color){
        return Drawables.getDrawable(color,20,0,color);
    }
}
