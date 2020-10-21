package cn.surine.schedulex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.interfaces.IBack
import cn.surine.schedulex.base.utils.StatusBars
import com.umeng.analytics.MobclickAgent

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 14:36
 */
class MainActivity : AppCompatActivity(), IBack {
    private var curFragment: BaseFragment? = null

    override fun onBackKeyClick(baseFragment: BaseFragment?) {
        curFragment = baseFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBars.setStatusBarUI(this, true)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        curFragment?.onBackPressed() ?: super.onBackPressed()
    }
}