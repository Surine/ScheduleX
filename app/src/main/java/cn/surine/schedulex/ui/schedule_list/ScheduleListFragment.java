package cn.surine.schedulex.ui.schedule_list;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentScheduleManagerBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_config.ScheduleConfigFragment;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;

import static cn.surine.schedulex.ui.schedule_config.ScheduleConfigFragment.SCHEDULE_ID;

public class ScheduleListFragment extends BaseBindingFragment<FragmentScheduleManagerBinding> {

    public static final String FUNCTION_TAG = "function_tag";
    private List<Schedule> data;
    private FragmentScheduleManagerBinding globalT;
    private ScheduleViewModel scheduleViewModel;
    private CourseViewModel courseViewModel;
    private BaseAdapter<Schedule> adapter;
    private LinearLayoutManager layoutManager;


    @Override
    public int layoutId() {
        return R.layout.fragment_schedule_manager;
    }


    @Override
    protected void onInit(FragmentScheduleManagerBinding t) {

        globalT = t;

        Class[] classesForSchedule = new Class[]{ScheduleRepository.class};
        Object[] argsForSchedule = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForSchedule, argsForSchedule)).get(ScheduleViewModel.class);


        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);


        data = scheduleViewModel.getSchedules();
        adapter = new BaseAdapter<>(data, R.layout.item_schedule_list, cn.surine.schedulex.BR.schedule);
        t.viewRecycler.setLayoutManager(layoutManager = new LinearLayoutManager(getActivity()));
        t.viewRecycler.setAdapter(adapter);

        t.viewRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    t.addSchedule.shrink();
                } else {
                    t.addSchedule.extend();
                }
            }
        });


        adapter.setOnItemClickListener(position -> {
            Long scheduleId = (long) data.get(position).roomId;
            if (!Prefs.getLong(Constants.CUR_SCHEDULE, -1L).equals(scheduleId)) {
                Prefs.save(Constants.CUR_SCHEDULE, scheduleId);
                adapter.notifyDataSetChanged();
                Snackbar.make(t.addSchedule, R.string.timetable_switched_successfully, Snackbar.LENGTH_SHORT).show();
            }
        });


        adapter.setOnItemLongClickListener(position -> {
            openScheduleSetting(position);
            return true;
        });


        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.importWay) {
            @Override
            public void onClick(View v, int position) {

            }
        });

        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.more_function) {
            @Override
            public void onClick(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_schedule, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.popup_menu_action_1:
                            openScheduleSetting(position);
                            break;
                        case R.id.popup_menu_action_2:
                            deleteSchedule(position);
                            break;
                        case R.id.popup_menu_action_4:
                            exportCourse(position);
                            break;
                    }
                    return false;
                });
                popupMenu.show();
            }
        });


        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.chip_config_name) {
            @Override
            public void onClick(View v, int position) {
                openScheduleSetting(position, ScheduleConfigFragment.CHANGE_SCHEDULE_NAME);
            }
        });

        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.chip_config_week) {
            @Override
            public void onClick(View v, int position) {
                openScheduleSetting(position, ScheduleConfigFragment.CHANGE_WEEK_INFO);
            }
        });

        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.chip_change_background) {
            @Override
            public void onClick(View v, int position) {
                openScheduleSetting(position, ScheduleConfigFragment.CHANGE_BACKGROUND);
            }
        });

        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.chip_course_item_height) {
            @Override
            public void onClick(View v, int position) {
                openScheduleSetting(position, ScheduleConfigFragment.CHANGE_COURSE_ITEM_HEIGHT);
            }
        });

        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.chip_more_setting) {
            @Override
            public void onClick(View v, int position) {
                openScheduleSetting(position);
            }
        });


        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.short_btn_add) {
            @Override
            public void onClick(View v, int position) {
                Toasts.toast("import");
            }
        });

        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.short_btn_share) {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(SCHEDULE_ID, data.get(position).roomId);
                NavHostFragment.findNavController(ScheduleListFragment.this).navigate(R.id.action_ScheduleListFragment_to_scheduleDataExport, bundle);
            }
        });


        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.short_btn_edit) {
            @Override
            public void onClick(View v, int position) {
                openScheduleSetting(position);
            }
        });

        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.short_btn_hide) {
            @Override
            public void onClick(View v, int position) {
                try {
                    View view = layoutManager.findViewByPosition(position);
                    View l2 = view.findViewById(R.id.layout_2);
                    View l3 = view.findViewById(R.id.layout_3);
                    Uis.hide(l2, l3);
                } catch (Exception e) {
                    CrashReport.postCatchedException(e);
                }
            }
        });

//        t.topbar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_ScheduleListFragment_to_aboutFragment));

        t.addSchedule.setOnClickListener(v -> {
            if (scheduleViewModel.getSchedulesNumber() < Constants.MAX_SCHEDULE_LIMIT) {
                Navigations.open(ScheduleListFragment.this, R.id.action_ScheduleListFragment_to_scheduleInitFragment);
            } else {
                Toasts.toast(getString(R.string.no_permission_to_add));
            }
        });
    }

    private void exportCourse(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(SCHEDULE_ID, data.get(position).roomId);
        NavHostFragment.findNavController(ScheduleListFragment.this).navigate(R.id.action_ScheduleListFragment_to_scheduleDataExport, bundle);
    }


    /**
     * 删除课表
     */
    private void deleteSchedule(int position) {
        if (scheduleViewModel.getCurSchedule().roomId == data.get(position).roomId) {
            Toasts.toast("不允许删除正在使用的课表");
        } else {
            CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.delete_schedule_dialog_msg)
                    , () -> {
                        //删除课表后需要删除所有相关课程
                        scheduleViewModel.deleteScheduleById(data.get(position).roomId);
                        courseViewModel.deleteCourseByScheduleId(data.get(position).roomId);
                        Toasts.toast(getString(R.string.schedule_is_delete));
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                    }, null).show();

        }
    }


    /**
     * 打开设置
     *
     * @param position 位置
     */
    private void openScheduleSetting(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(SCHEDULE_ID, data.get(position).roomId);
        Navigations.open(ScheduleListFragment.this, R.id.action_ScheduleListFragment_to_scheduleConfigFragment, bundle);
    }


    /**
     * 打开设置
     *
     * @param position 位置
     * @param tag      功能标识
     */
    private void openScheduleSetting(int position, int tag) {
        Bundle bundle = new Bundle();
        bundle.putInt(SCHEDULE_ID, data.get(position).roomId);
        bundle.putInt(FUNCTION_TAG, tag);
        Navigations.open(ScheduleListFragment.this, R.id.action_ScheduleListFragment_to_scheduleConfigFragment, bundle);
    }

}
