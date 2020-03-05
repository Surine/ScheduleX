package cn.surine.schedulex.base.manager;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Intro：
 * 动画管理器
 * @author sunliwei
 * @date 2020-03-03 11:33
 */
public class AnimationManager {

    /**
     * 加载页面元素动画
     * @param view 按照动画加载顺序传入view，实现页面加载动效
     * */
    public void loadUiElement(View... view){
        for (int i = 0; i < view.length; i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, 100f);
            animator.start();
        }

    }
}
