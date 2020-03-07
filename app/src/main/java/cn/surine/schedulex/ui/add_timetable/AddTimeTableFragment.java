package cn.surine.schedulex.ui.add_timetable;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.data.entity.TimeTable;
import cn.surine.schedulex.data.entity.TimeTableDisplayEntity;
import cn.surine.schedulex.databinding.FragmentAddTimetableBinding;
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

    @Override
    public int layoutId() {
        return R.layout.fragment_add_timetable;
    }


    @Override
    protected void onInit(FragmentAddTimetableBinding t) {

        Class[] classesForTimeTable = new Class[]{TimeTableRepository.class};
        Object[] argsForTimeTable = new Object[]{TimeTableRepository.abt.getInstance()};
        timeTableViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForTimeTable, argsForTimeTable)).get(TimeTableViewModel.class);

//        dataMappingTT2TD(timeTableData = timeTableViewModel.getTimTableById(0));

        timeTable = new TimeTable("课表");
        timeTable.startTime = 8 * 60 + 20;
        timeTable.rule = "45,10,45,20,45,10,45,120,45,10,45,20,45,10,45,0";

        t.tvSessionText.setText((mSessionTime = Integer.parseInt(timeTable.rule.split(",")[0])) + "分钟");
        t.slider.setValue(mSessionTime);
        t.slider.addOnChangeListener((slider, value, fromUser) -> {
            int oldSessionTime = mSessionTime;
            mSessionTime = (int) value;
            t.tvSessionText.setText(mSessionTime + "分钟");
            updateClassTime(0, oldSessionTime - mSessionTime, false);
        });

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

        adapter.setOnItemClickListener(position -> CommonDialogs.timePickerDialog(activity(), "请选择本节课上课时间", Integer.parseInt(data.get(position).startTime.split(":")[0]), Integer.parseInt(data.get(position).startTime.split(":")[1]), (view, hourOfDay, minute) -> {
            //如果选择的时间不合理就提示
            if (position > 0 && (hourOfDay * 60 + minute) < Dates.getTransformTimeString(data.get(position - 1).endTime)) {
                Snackbar.make(t.addTimeTable, "本节上课时间不得比上一节下课时间早", Snackbar.LENGTH_SHORT).show();
            } else {
                int mTimeOffset = (int) (Dates.getTransformTimeString(data.get(position).startTime) - (hourOfDay * 60 + minute));
                updateClassTime(position, mTimeOffset, true);
            }
        }).show());


        t.addTimeTable.setOnClickListener(v -> {
            dataMappingTD2TT(data);
            Log.d("slw", "onClick: " + timeTable.toString());
        });
    }


    private void updateClassTime(int position, int mTimeOffset, boolean isStartTime) {
        //如果是点击了slider调节每节课时长
        if (!isStartTime) {
            //则更新一下第一节的终止时间，然后继续更新后面的内容
            long curStartTime = Dates.getTransformTimeString(data.get(0).startTime);
            data.get(0).endTime = Dates.getTransformTimeNumber(curStartTime + mSessionTime);
            adapter.notifyItemChanged(0);
        }

        //否则就直接更新从position开始的所有数据即可
        for (int i = isStartTime ? position : position + 1; i < data.size(); i++) {
            //起始时间
            data.get(i).startTime = Dates.getTransformTimeNumber(Dates.getTransformTimeString(data.get(i).startTime) - mTimeOffset);
            //结束时间
            long curStartTime = Dates.getTransformTimeString(data.get(i).startTime);
            data.get(i).endTime = Dates.getTransformTimeNumber(curStartTime + mSessionTime);
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
     */
    private void dataMappingTD2TT(List<TimeTableDisplayEntity> timeTableDisplayEntities) {
        StringBuilder stringBuilder = new StringBuilder();
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
        timeTable.rule = stringBuilder.toString();
    }
}
