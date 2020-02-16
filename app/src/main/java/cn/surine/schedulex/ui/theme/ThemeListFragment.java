package cn.surine.schedulex.ui.theme;

import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.data.entity.Theme;

public class ThemeListFragment extends BaseBindingFragment {


    private List<Theme> data;

    @Override
    public int layoutId() {
        return R.layout.fragment_theme_list;
    }


    @Override
    protected void onInit(Object t) {

    }
}
