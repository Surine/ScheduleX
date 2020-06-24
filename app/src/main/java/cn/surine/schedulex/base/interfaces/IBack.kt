package cn.surine.schedulex.base.interfaces

import cn.surine.schedulex.base.controller.BaseFragment

/**
 * Intro: 配合Navigation处理返回事件
 *
 * @author sunliwei
 * @date 2020-01-26 18:48
 */
interface IBack {
    /**
     * 处理返回事件
     *
     * @param baseFragment
     */
    fun onBackKeyClick(baseFragment: BaseFragment?)
}