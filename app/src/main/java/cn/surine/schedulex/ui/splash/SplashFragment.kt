package cn.surine.schedulex.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import cn.surine.schedulex.R
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Navigations.open
import cn.surine.schedulex.base.utils.Prefs.getBoolean

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 15:48
 */
class SplashFragment : BaseFragment() {
    private val handler: Handler? = Handler()
    private lateinit var runnable: Runnable
    override fun layoutId(): Int = R.layout.fragment_splash
    override fun onInit(parent: View?) {
        runnable = Runnable {
            open(this, if (!getBoolean(Constants.IS_FIRST, false)) R.id.action_splashFragment_to_dataFetchFragment else R.id.action_splashFragment_to_scheduleFragment, Bundle())
        }
        handler?.postDelayed(runnable, 1200)
    }

    override fun onStop() {
        super.onStop()
        handler?.removeCallbacks(runnable)
    }

}