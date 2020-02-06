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
     * */
    @CallSuper
    public void onBackPressed(){
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
        View view = inflater.inflate(layoutId(), container, false);

        onInit(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getActivity() instanceof IBack){
            this.iBack = (IBack) getActivity();
            //此时将返回键任务交给fragment
            iBack.onBackKeyClick(this);
        }else {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }
    }
}
