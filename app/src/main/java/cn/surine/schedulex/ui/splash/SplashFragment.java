package cn.surine.schedulex.ui.splash;

import android.os.Handler;
import android.view.View;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseFragment;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Prefs;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-26 19:17
 */
public class SplashFragment extends BaseFragment {
    private Handler mHandler = new Handler();
    private Runnable runnable;

    @Override
    public int layoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    public void onInit(View parent) {
        runnable = () -> {
            if(!Prefs.getBoolean(Constants.IS_FIRST, false)){
                Navigations.open(SplashFragment.this,R.id.action_splashFragment_to_scheduleInitFragment);
            }else{
                Navigations.open(SplashFragment.this,R.id.action_splashFragment_to_scheduleFragment);
            }
        };

        if(mHandler != null){
            mHandler.postDelayed(runnable,1500);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(runnable);
        mHandler = null;
    }
}
