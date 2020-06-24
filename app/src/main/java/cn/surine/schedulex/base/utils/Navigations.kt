package cn.surine.schedulex.base.utils

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import cn.surine.schedulex.base.controller.BaseFragment

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 11:52
 */
object Navigations {

    /**
     * 打开页面
     * @param fragment 调用方
     * @param id navid
     * @param bundle 参数
     * */
    @JvmStatic
    fun open(fragment: BaseFragment?, id: Int, bundle: Bundle?) {
        fragment ?: return
        bundle ?: return
        NavHostFragment.findNavController(fragment).navigate(id, bundle)
    }


    /**
     * 打开页面 兼容旧版本
     * @param fragment 调用方
     * @param id navid
     * */
    @JvmStatic
    fun open(fragment: BaseFragment?, id: Int) {
        fragment ?: return
        NavHostFragment.findNavController(fragment).navigate(id)
    }


    /**
     * 关闭页面
     * @param fragment 调用方
     * */
    @JvmStatic
    fun close(fragment: BaseFragment?) {
        fragment ?: return
        NavHostFragment.findNavController(fragment).navigateUp()
    }

}