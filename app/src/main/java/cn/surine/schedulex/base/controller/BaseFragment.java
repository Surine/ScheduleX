package cn.surine.schedulex.base.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import cn.surine.schedulex.base.interfaces.IBack;
import cn.surine.schedulex.base.utils.StatusBars;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-15 20:31
 */
public abstract class BaseFragment extends Fragment {

    protected IBack iBack;

    /**
     * Host activity
     */
    protected ViewDataBinding dataBind;
    private Activity activity;

    /**
     * override this method return the fragment layout id
     */
    public abstract int layoutId();

    /**
     * override this method and initialization the fragment
     *
     * @param parent root view of the fragment
     */
    public abstract void onInit(View parent);


    /**
     * the method for back key pressed
     */
    @CallSuper
    public void onBackPressed() {
        NavHostFragment.findNavController(this).navigateUp();
    }


    public Activity activity() {
        return getActivity() == null ? activity : getActivity();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity && activity == null) {
            this.activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutId(), container, false);
    }

    /**
     * 6月22 遇到一个问题 就是使用kotlin资源id生成器返回的资源为null
     * 应该是在资源变量还没完全创建好的时候就调用了onInit方法，这里改动为
     * 在onViewCreated生命周期下调用，以确保使用时资源都已经加载完成
     * */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInit(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof IBack) {
            this.iBack = (IBack) getActivity();
            //此时将返回键任务交给fragment
            iBack.onBackKeyClick(this);
        } else {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        statusBarUi();
    }

    protected void statusBarUi() {
        StatusBars.setStatusBarUI(activity(), true);
    }


    public BaseFragment fragment() {
        return this;
    }

}
