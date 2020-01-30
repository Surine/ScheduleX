package cn.surine.schedulex.base.utils;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Intro：数据绑定工具
 * @author sunliwei
 * @date 2020-01-15 19:38
 */
public class Bindings {

    /**
     * 绑定
     * @param parent 父布局
     * @param layoutId 目标布局
     * */
    public static ViewDataBinding bind(ViewGroup parent,int layoutId){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return DataBindingUtil.inflate(layoutInflater,layoutId,parent,false);
    }
}
