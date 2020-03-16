package cn.surine.schedulex.ui.add_timetable;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.TimeTable;
import cn.surine.schedulex.data.entity.TimeTableDisplayEntity;
import cn.surine.schedulex.databinding.FragmentAddTimetableBinding;
import cn.surine.schedulex.ui.timetable_list.TimeTableListFragment;
import cn.surine.schedulex.ui.timetable_list.TimeTableRepository;
import cn.surine.schedulex.ui.timetable_list.TimeTableViewModel;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-03-06 15:10
 */
public class AddTimeTableFragment extends BaseBindingFragment<FragmentAddTimetableBinding> {
    private BaseAdapter<TimeTableDisplayEntity> adapter;
    private List<TimeTableDisplayEntity> data = new ArrayList<>();
    private TimeTableViewModel timeTableViewModel;
    //每节课时长
    private int mSessionTime = 10;
    private TimeTable timeTable;
    private boolean isAdd;
    private FragmentAddTimetableBinding globalT;

    @Override
    public int layoutId() {
        return R.layout.fragment_add_timetable;
    }


    @SuppressLint({"SetTextI18n", "StringFormatMatches"})
    @Override
    protected void onInit(FragmentAddTimetableBinding t) {
        globalT = t;
        Class[] classesForTimeTable = new Class[]{TimeTableRepository.class};
        Object[] argsForTimeTable = new Object[]{TimeTableRepository.abt.getInstance()};
        timeTableViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForTimeTable, argsForTimeTable)).get(TimeTableViewModel.class);

        if (getArguments() != null) {
            timeTable = timeTableViewModel.getTimTableById(getArguments().getInt(TimeTableListFragment.TIMETABLE_ID, -1));
            isAdd = false;
        } else {
            timeTable = TimeTable.tedaNormal();
            timeTable.name = "";
            isAdd = true;
        }
        t.editText.setText(timeTable.name);
        t.tvSessionText.setText((mSessionTime = Integer.parseInt(timeTable.rule.split(",")[0])) + "分钟");

        t.settingItemEverySessionTime.setOnClickListener(v -> showSessionTimeDialog());

//        t.slider.setValue(mSessionTime);
//        t.slider.addOnChangeListener((slider, value, fromUser) -> {
//            mSessionTime = (int) value;
//            t.tvSessionText.setText(mSessionTime + "分钟");
//            changeSessionTime(0);
//        });

        dataMappingTT2TD(timeTable);
        adapter = new BaseAdapter<>(data, R.layout.item_time_table_session_info, cn.surine.schedulex.BR.timeDisplay);
        t.timetableList.setLayoutManager(new LinearLayoutManager(activity()));
        t.timetableList.setAdapter(adapter);

        t.timetableList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    t.addTimeTable.shrink();
                } else {
                    t.addTimeTable.extend();
                }
            }
        });

        adapter.setOnItemClickListener(position -> CommonDialogs.timePickerDialog(activity(), getString(R.string.please_choose_time), Integer.parseInt(data.get(position).startTime.split(":")[0]), Integer.parseInt(data.get(position).startTime.split(":")[1]), (view, hourOfDay, minute) -> {
            //如果选择的时间不合理就提示
            if (position > 0 && (hourOfDay * 60 + minute) < Dates.getTransformTimeString(data.get(position - 1).endTime)) {
                Snackbar.make(t.addTimeTable, R.string.time_rule, Snackbar.LENGTH_SHORT).show();
            } else {
                int mTimeOffset = (int) (Dates.getTransformTimeString(data.get(position).startTime) - (hourOfDay * 60 + minute));
                changeStartTimeUpdate(position, mTimeOffset);
            }
        }).show());


        t.addSession.setOnClickListener(v -> {
            TimeTableDisplayEntity entity = data.get(data.size() - 1);
            long lastEndTime = Dates.getTransformTimeString(entity.endTime);
            if (entity.session >= Constants.MAX_SESSION) {
                Toasts.toast(getString(R.string.max_support, Constants.MAX_SESSION));
                return;
            }
            if (lastEndTime + 10 > 24 * 60 || lastEndTime + 10 + mSessionTime > 24 * 60) {
                Toasts.toast(getString(R.string.need_today));
                return;
            }
            data.add(new TimeTableDisplayEntity(entity.session + 1, Dates.getTransformTimeNumber(lastEndTime + 10), Dates.getTransformTimeNumber(lastEndTime + 10 + mSessionTime)));
            adapter.notifyItemInserted(data.size());
            t.timetableList.smoothScrollToPosition(data.size());
        });


        adapter.setOnItemLongClickListener(position -> {
            if (position < 12) {
                Toasts.toast(getString(R.string.delete_rule));
            } else {
                CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.delete_timetable_session_note), () -> {
                    for (int i = data.size() - 1; i >= position; i--) {
                        data.remove(i);
                    }
                    adapter.notifyDataSetChanged();
                    Toasts.toast(getString(R.string.delete));
                }, null).show();
            }
            return true;
        });


        t.addTimeTable.setOnClickListener(v -> {
            if (TextUtils.isEmpty(t.editText.getText().toString())) {
                Toasts.toast(getString(R.string.param_empty));
                return;
            }
            timeTable.name = t.editText.getText().toString();
            boolean checkStatus = dataMappingTD2TT(data);
            if (!checkStatus) {
                Toasts.toast(getString(R.string.need_today));
                return;
            }
            if (isAdd) {
                timeTableViewModel.addTimeTable(timeTable);
            } else {
                timeTableViewModel.updateTimeTable(timeTable);
            }
            Toasts.toast(getString(R.string.handle_success));
            Navigations.close(AddTimeTableFragment.this);
        });
    }

    //展示
    @SuppressLint("SetTextI18n")
    private void showSessionTimeDialog() {
        BottomSheetDialog bt = new BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme);
        bt.setDismissWithAnimation(true);
        View baseView;
        bt.setContentView(baseView = Uis.inflate(activity(), R.layout.view_base_btm_dialog_ui));
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.show();

        FrameLayout frameLayout = baseView.findViewById(R.id.view_base_btm_dialog_ui);
        View view = Uis.inflate(activity(), R.layout.view_number_picker);
        baseView.animate().translationY(20);
        view.animate().translationY(50);
        frameLayout.addView(view);
        ((TextView) view.findViewById(R.id.dialog_title)).setText(R.string.setting_one_session_time);
        NumberPicker numberPicker = view.findViewById(R.id.number_picker);

        String[] times = new String[23];
        for (int i = 0; i < 23; i++) {
            times[i] = String.valueOf(i * 5 + 10);
        }
        numberPicker.setDisplayedValues(times);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(times.length - 1);
        numberPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);


        view.findViewById(R.id.button).setOnClickListener(v -> {
            mSessionTime = Integer.parseInt(times[numberPicker.getValue()]);
            globalT.tvSessionText.setText(mSessionTime + getString(R.string.min));
            changeSessionTime(0);
            bt.dismiss();
        });

    }


    //如果是修改了上课时间
    private void changeStartTimeUpdate(int position, int mTimeOffset) {
        for (int i = position; i < data.size(); i++) {
            //起始时间
            data.get(i).startTime = Dates.getTransformTimeNumber(Dates.getTransformTimeString(data.get(i).startTime) - mTimeOffset);
            //结束时间
            long curStartTime = Dates.getTransformTimeString(data.get(i).startTime);
            data.get(i).endTime = Dates.getTransformTimeNumber(curStartTime + mSessionTime);
            adapter.notifyItemChanged(i);
        }
    }

    //如果是修改了每节课的时长
    private void changeSessionTime(int position) {
        //先记下旧的第一节下课时间
        long classOverTime = Dates.getTransformTimeString(data.get(0).endTime);
        //然后更新一下第一节的终止时间，然后继续更新后面的内容
        long curStartTime = Dates.getTransformTimeString(data.get(0).startTime);
        data.get(0).endTime = Dates.getTransformTimeNumber(curStartTime + mSessionTime);
        adapter.notifyItemChanged(0);

        for (int i = position + 1; i < data.size(); i++) {
            //从第2个开始
            //计算起始时间 = 新的上一节终止时间 + 课间时间（旧的本节开始时间 - 旧的上节终止时间）
            data.get(i).startTime = Dates.getTransformTimeNumber(Dates.getTransformTimeString(data.get(i - 1).endTime) + Dates.getTransformTimeString(data.get(i).startTime) - classOverTime);

            //重新赋值(旧的本节终止时间，留给下一轮循环使用)
            classOverTime = Dates.getTransformTimeString(data.get(i).endTime);

            //新的本节结束时间
            long curStartTime2 = Dates.getTransformTimeString(data.get(i).startTime);
            data.get(i).endTime = Dates.getTransformTimeNumber(curStartTime2 + mSessionTime);
            adapter.notifyItemChanged(i);

        }
    }


    /**
     * 数据映射 timetable -> timedisplay
     *
     * @param timeTable
     */
    private void dataMappingTT2TD(TimeTable timeTable) {
        String[] ruleData = timeTable.rule.split(",");
        long time = timeTable.startTime;
        for (int i = 0, j = 1; i < ruleData.length - 1; j++, i += 2) {
            TimeTableDisplayEntity timeTableDisplayEntity = new TimeTableDisplayEntity();
            timeTableDisplayEntity.session = j;
            timeTableDisplayEntity.startTime = Dates.getTransformTimeNumber(time);
            timeTableDisplayEntity.endTime = Dates.getTransformTimeNumber(time += Integer.parseInt(ruleData[i]));
            data.add(timeTableDisplayEntity);
            time += Integer.parseInt(ruleData[i + 1]);
        }
    }


    /**
     * 数据映射 timedisplay -> timetable
     *
     * @return
     */
    private boolean dataMappingTD2TT(List<TimeTableDisplayEntity> timeTableDisplayEntities) {
        StringBuilder stringBuilder = new StringBuilder();
        long tmp;
        for (int i = 0; i < timeTableDisplayEntities.size(); i++) {
            if (i == 0) {
                timeTable.startTime = Dates.getTransformTimeString(timeTableDisplayEntities.get(i).startTime);
            }
            //一节课
            stringBuilder.append(Dates.getTransformTimeString(timeTableDisplayEntities.get(i).endTime) - Dates.getTransformTimeString(timeTableDisplayEntities.get(i).startTime)).append(",");
            //课间
            if (i != timeTableDisplayEntities.size() - 1) {
                stringBuilder.append(Dates.getTransformTimeString(timeTableDisplayEntities.get(i + 1).startTime) - Dates.getTransformTimeString(timeTableDisplayEntities.get(i).endTime)).append(",");
            }
        }
        //防止越界安全
        stringBuilder.append("0");
        //不允许包含负数，一般是咵天导致的
        if (stringBuilder.toString().contains("-")) {
            return false;
        } else {
            timeTable.rule = stringBuilder.toString();
            return true;
        }
    }
}
