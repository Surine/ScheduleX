package cn.surine.schedulex.base.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import cn.surine.schedulex.base.interfaces.IBack;

/**
 * Intro：
 * 绑定用base fragment
 *
 * @author sunliwei
 * @date 2020-01-17 11:52
 */
public abstract class BaseBindingFragment<D> extends BaseFragment {
    private D d;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBind = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        d = (D) dataBind;
        if(getActivity() instanceof IBack){
            this.iBack = (IBack) getActivity();
        }else{
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }
        onInit(d);
        dataBind.setLifecycleOwner(this);
        return dataBind.getRoot();
    }

    @CallSuper
    @Override
    public void onInit(View parent) {
        onInit(d);
    }

    protected abstract void onInit(D t);

    @Override
    public void onStart() {
        super.onStart();
        iBack.onBackKeyClick(this);
    }
}
