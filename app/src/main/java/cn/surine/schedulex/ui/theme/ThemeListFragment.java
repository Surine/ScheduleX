package cn.surine.schedulex.ui.theme;

import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.data.entity.Theme;

public class ThemeListFragment extends BaseBindingFragment {


    private ThemeListViewModel themeListFragment;
    private List<Theme> data;

    @Override
    public int layoutId() {
        return R.layout.fragment_theme_list;
    }


    @Override
    protected void onInit(Object t) {
//        themeListFragment = ViewModelProviders.of(this).get(ThemeListViewModel.class);
//
//        data = themeListFragment.getThemeData();
//        BaseAdapter<Theme> adapter = new BaseAdapter<>(data, R.layout.item_theme_list, cn.surine.schedulex.BR.theme);
//        RecyclerView recyclerview = (RecyclerView) t.viewRecycler;
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity());
//        recyclerview.setLayoutManager(linearLayoutManager);
//        recyclerview.setAdapter(adapter);
//
//        adapter.setOnItemClickListener(position -> {
//            Long scheduleId = (long) data.get(position).roomId;
//            Prefs.save(Constants.CUR_SCHEDULE, scheduleId);
//            adapter.notifyDataSetChanged();
//        });
    }
}
