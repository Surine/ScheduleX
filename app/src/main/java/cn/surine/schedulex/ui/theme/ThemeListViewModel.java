package cn.surine.schedulex.ui.theme;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.data.entity.Theme;

public class ThemeListViewModel extends ViewModel {
    public List<Theme> getThemeData() {
        return new ArrayList<>();
    }

}
