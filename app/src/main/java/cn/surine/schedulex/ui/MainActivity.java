package cn.surine.schedulex.ui;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseFragment;
import cn.surine.schedulex.base.interfaces.IBack;
import cn.surine.schedulex.base.utils.StatusBars;

import static androidx.lifecycle.ViewModelProviders.of;
import static cn.surine.schedulex.base.utils.Uis.getDarkModeStatus;


/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-17 10:59
 */
public class MainActivity extends AppCompatActivity implements IBack{
    MainViewModel viewModel;
    private BaseFragment curFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBars.setStatusBarUI(this,true);
        setContentView(R.layout.activity_main);
        viewModel = of(this).get(MainViewModel.class);
    }

    @Override
    public void onBackKeyClick(BaseFragment baseFragment) {
        this.curFragment = baseFragment;
    }

    public static class MainViewModel extends AndroidViewModel {
        public MainViewModel(@NonNull Application application) {
            super(application);
        }
    }


    @Override
    public void onBackPressed() {
        if(curFragment != null){
            curFragment.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }
}

