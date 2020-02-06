package cn.surine.schedulex.ui.schedule;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseFragment;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-26 21:21
 */
public class ScheduleViewpagerAdapter extends RecyclerView.Adapter<ScheduleViewpagerAdapter.ViewPagerViewHolder> {

    public static final String COURSE_ID = "course_id";
    private List<List<Course>> courseList;
    private BaseFragment baseFragment;

    public ScheduleViewpagerAdapter(List<List<Course>> courseList, BaseFragment baseFragment) {
        this.courseList = courseList;
        this.baseFragment = baseFragment;
    }


    public interface OnScrollBindListener {
        void onScroll(int position);
    }


    private OnScrollBindListener onScrollBindListener;

    public void setOnScrollBindListener(OnScrollBindListener onScrollBindListener) {
        this.onScrollBindListener = onScrollBindListener;
    }

    @NonNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewPagerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
        //课程表
        BaseAdapter<Course> adapter = new BaseAdapter<>(courseList.get(position), R.layout.item_course_view, cn.surine.schedulex.BR.course);
        adapter.setBanRecycle(true);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(baseFragment.activity(), 5));
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.scrollTo(0, 0);
        //设置滚动监听器
        setScrollerListener(holder.recyclerView);

        adapter.setOnItemClickListener(courseDataPosition -> {
            Course course = courseList.get(position).get(courseDataPosition);
            if (course == null) {
                return;
            }
            BtmDialogs.showCourseInfoBtmDialog(baseFragment, course);
        });
    }

    private void setScrollerListener(RecyclerView recyclerView) {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrollBindListener != null) {
                    onScrollBindListener.onScroll(dy);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class ViewPagerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.schedule_view);
        }
    }
}
