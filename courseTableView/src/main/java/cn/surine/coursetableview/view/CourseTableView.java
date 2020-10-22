package cn.surine.coursetableview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.surine.coursetableview.R;
import cn.surine.coursetableview.entity.BCourse;
import cn.surine.coursetableview.entity.BTimeTable;
import cn.surine.coursetableview.interfaces.OnClickCourseItemListener;
import cn.surine.coursetableview.utils.Drawables;
import cn.surine.coursetableview.utils.TimeUtil;

import static cn.surine.coursetableview.utils.TimeUtil.getWeekDay;

/**
 * Created by Surine on 2019/2/25.
 * 课程表视图
 */

public class CourseTableView extends LinearLayout {

    private static final float MAIN_TEXT_SIZE = 12;
    int w = View.MeasureSpec.makeMeasureSpec(0,
            View.MeasureSpec.UNSPECIFIED);
    int h = View.MeasureSpec.makeMeasureSpec(0,
            View.MeasureSpec.UNSPECIFIED);

    /*UI 配置器*/
    private UIConfig mUiConfig = new UIConfig();
    /*数据集 配置器*/
    private DataConfig mDataConfig = new DataConfig();
    /*点击监听器*/
    private OnClickCourseItemListener mClickCourseItemListener;
    private OnClickCourseItemListener mLongClickCourseItemListener;
    /*周view视图*/
    private LinearLayout mWeekView;
    /*节次view视图*/
    private LinearLayout mSectionView;
    /*Week Panel 视图*/
    private RelativeLayout weekPanel1;
    private RelativeLayout weekPanel2;
    private RelativeLayout weekPanel3;
    private RelativeLayout weekPanel4;
    private RelativeLayout weekPanel5;
    private RelativeLayout weekPanel6;
    private RelativeLayout weekPanel7;
    private List<RelativeLayout> layoutList = new ArrayList<>();
    /*上下文*/
    private Context mContext;
    /*侧边栏是否加载*/
    private boolean isSectionViewInit = false;
    /*最大周临时变量*/
    private int myMaxWeekNum = 0;

    public CourseTableView(Context context) {
        super(context);
        init(context);
    }

    public CourseTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CourseTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CourseTableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    /**
     * 更新UI及数据集
     *
     * @param uiConfig   UI配置
     * @param dataConfig
     * @return this
     */
    public CourseTableView update(UIConfig uiConfig, DataConfig dataConfig) {
        mUiConfig = uiConfig;
        mDataConfig = dataConfig;

        updateWeekView();  //加载周视图
        updateSectionView(); //加载侧边栏
        updateData();  //加载数据

        return this;
    }

    /**
     * 更新数据集
     *
     * @param dataConfig 数据集
     * @return this
     */
    public CourseTableView update(DataConfig dataConfig) {
        mDataConfig = dataConfig;
        //数据加载
        //如果侧边栏未加载则进行加载
        if (!isSectionViewInit) {
            updateSectionView();
        }

        //顶部栏要跟着更新时间数据
        updateWeekView();
        updateData();

        return this;
    }

    /**
     * 更新UI
     *
     * @param uiConfig UI配置
     * @return this
     */
    public CourseTableView update(UIConfig uiConfig) {
        return update(uiConfig, mDataConfig);
    }

    /**
     * 获取UI配置
     *
     * @return UIConfig
     */
    public UIConfig getUiConfig() {
        return mUiConfig;
    }

    /**
     * 获取课程数据配置
     *
     * @return DataConfig
     */
    public DataConfig getDataConfig() {
        return mDataConfig;
    }


    /**
     * 根据当前设置的开学日期算出现在是第几周
     *
     * @return 返回当前周，负数代表当前在开学日期过去，正数在之前
     */
    public int getRealWeekByCurTermStartDate() {
        try {
            return TimeUtil.getRealWeek(mDataConfig.getCurTermStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 重载
     */
    public int getRealWeekByCurTermStartDate(String date) {
        try {
            return TimeUtil.getRealWeek(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 配置监听器
     *
     * @param mClickCourseItemListener 监听器
     */
    public void setmClickCourseItemListener(OnClickCourseItemListener mClickCourseItemListener) {
        this.mClickCourseItemListener = mClickCourseItemListener;
    }


    /*初始化周视图*/
    private void initWeekView(int maxClassDay) {
        //清空
        mWeekView.removeViews(0, mWeekView.getChildCount());
        for (int i = 0; i <= maxClassDay; i++) {
            TextView tvWeekName = new TextView(mContext);

            if (i == 0) {
                LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(mUiConfig.getSectionViewWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                lp0.gravity = Gravity.CENTER;
                tvWeekName.setText(TimeUtil.getWeekInfoString(mDataConfig.getCurTermStartDate(), mDataConfig.getCurrentWeek()) + "\n月");
                tvWeekName.setTextColor(mUiConfig.getColorUI());
                tvWeekName.setTextSize(MAIN_TEXT_SIZE);
                tvWeekName.setGravity(Gravity.CENTER);
                tvWeekName.setLayoutParams(lp0);

                //不显示月份（可走配置）
                tvWeekName.setVisibility(INVISIBLE);
            } else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                lp.weight = 1f;
                String today = TimeUtil.getTodayInfoString(mDataConfig.getCurTermStartDate(), mDataConfig.getCurrentWeek(), i);
                tvWeekName.setText(mUiConfig.getWeekInfoStyle()[i - 1] + "\n" + today);
                int color = (i == getWeekDay()) ? mUiConfig.getChooseWeekColor() : mUiConfig.getColorUI();
                tvWeekName.setTextColor(color);

                if (i == getWeekDay()) {
                    tvWeekName.setPadding(0, 5, 0, 5);
                    tvWeekName.setBackground(Drawables.getDrawable(getResources().getColor(R.color.choose_tag), 20, 0, 0));
                    tvWeekName.setTextColor(Color.WHITE);
                }

                tvWeekName.setTextSize(MAIN_TEXT_SIZE);
                tvWeekName.getPaint().setFakeBoldText(i == getWeekDay());
                tvWeekName.setGravity(Gravity.CENTER);
                tvWeekName.setLayoutParams(lp);
            }
            mWeekView.addView(tvWeekName);
        }

    }

    /*初始化课节次布局*/
    @SuppressLint("SetTextI18n")
    private void initSectionView(int maxSection) {

        LinearLayout.LayoutParams lpx = new LinearLayout.LayoutParams(mUiConfig.getSectionViewWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        mSectionView.setLayoutParams(lpx);
        mSectionView.removeViews(0, mSectionView.getChildCount());

        BTimeTable bTimeTable = mDataConfig.getTimeTable();

        //计算当前时间
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(date);
        long curTimeNum = transTime(time);

        for (int i = 1; i <= maxSection; i++) {
            TextView tvSection = new TextView(mContext);
            tvSection.setTextColor(mUiConfig.getColorUI());
            //侧边栏单项高度 = 小课课高度 + padding
            int height = mUiConfig.getSectionHeight() + mUiConfig.getItemTopMargin();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
            lp.gravity = Gravity.CENTER;
            tvSection.setGravity(Gravity.CENTER);
            tvSection.setTextSize(12);

            int numberLen = String.valueOf(i).length();
            SpannableString ss;
            List<BTimeTable.BTimeInfo> timeInfoList;
            if (bTimeTable != null && (timeInfoList = bTimeTable.timeInfoList) != null && timeInfoList.size() > 0 && getUiConfig().isShowTimeTable()) {
                try {
                    String startTime = timeInfoList.get(i - 1).startTime;
                    String endTime = timeInfoList.get(i - 1).endTime;
                    ss = new SpannableString(i + "\n" + startTime + "\n" + endTime);
                    if (curTimeNum > transTime(startTime) && curTimeNum < transTime(endTime)) {
                        tvSection.setTextColor(mUiConfig.getChooseWeekColor());
                    }
                } catch (Exception e) {
                    ss = new SpannableString(String.valueOf(i));
                }
            } else {
                ss = new SpannableString(String.valueOf(i));
            }
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.8f);
            ss.setSpan(relativeSizeSpan, numberLen, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSection.setText(ss);
            tvSection.setLayoutParams(lp);
            mSectionView.addView(tvSection);
        }
    }


    /**
     * 转换时间
     */
    private long transTime(String time) {
        return Integer.parseInt(time.split(":")[0]) * 60 + Integer.parseInt(time.split(":")[1]);
    }


    /*加载课程项目信息  */
    @SuppressLint("SetTextI18n")
    private void initCourseItemView() {

        //清除布局
        for (int j = 0; j < 7; j++) {
            layoutList.get(j).removeAllViews();
        }

        List<BCourse> mBcourses = mDataConfig.getCourseList();
        int courseSize = mBcourses.size();
        if (courseSize <= 0) {
            return;
        }

        //再次判断当前周不合法
        if (mDataConfig.getCurrentWeek() > mDataConfig.getMaxWeek()) {
            mDataConfig.setCurrentWeek(mDataConfig.getMaxWeek());
        }

        loadData(mBcourses);


        //课表显示与隐藏
        if (mUiConfig.getMaxClassDay() == 5) {
            layoutList.get(5).setVisibility(GONE);
            layoutList.get(6).setVisibility(GONE);
        } else {
            layoutList.get(5).setVisibility(VISIBLE);
            layoutList.get(6).setVisibility(VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadData(List<BCourse> mBcourses) {
        int itemPosition;

        //height flag position,day flag position
        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < mBcourses.size(); i++) {
            itemPosition = i;
            boolean isCurWeek;
            BCourse bCourse = mBcourses.get(i);
            if (bCourse == null || bCourse.getSectionStart() >= mUiConfig.getMaxSection() + 1) {
                continue;
            }

            //check the position
            if (bCourse.getSectionStart() + bCourse.getSectionContinue() > mUiConfig.getMaxSection() + 1) {
                bCourse.setSectionContinue(mUiConfig.getMaxSection() + 1 - bCourse.getSectionStart());
            }

            //is this week
            int thisDay = bCourse.getDay() - 1;
            RelativeLayout curDayLayout = layoutList.get(thisDay);
            isCurWeek = bCourse.getWeek().contains(mDataConfig.getCurrentWeek());
            //first to load curweek class
            if (!isCurWeek) {
                continue;
            }

            FrameLayout frameLayout = new FrameLayout(getContext());
            TextView tv = new TextView(mContext);
            //course height = class continue * little section height + gap * padding
            int bCourseHeight = bCourse.getSectionContinue() * mUiConfig.getSectionHeight()
                    + (bCourse.getSectionContinue() - 1) * mUiConfig.getItemTopMargin();
            RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    bCourseHeight);
            flp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            //class gap
            int topMargin = (bCourse.getSectionStart() - 1) * mUiConfig.getSectionHeight()
                    + bCourse.getSectionStart() * mUiConfig.getItemTopMargin();
            flp.setMargins(0, topMargin, 0, 0);
            //is cur week
            if (bCourse.getColor() != null) {
                frameLayout.setBackground(Drawables.getDrawable(Color.parseColor(bCourse.getColor()), 20, 3, Color.WHITE));
            } else {
                frameLayout.setBackground(Drawables.getDrawable(Color.TRANSPARENT, 20, 3, Color.WHITE));
            }
            tv.setTextColor(Color.WHITE);
            tv.setText(bCourse.getSimpleName(mUiConfig.getMaxHideCharLimit()) + "@" + bCourse.getPosition());
            tv.setLayoutParams(tlp);
            tv.setPadding(10, 10, 10, 10);
            tv.getPaint().setFakeBoldText(true);
            tv.setGravity(UIConfig.POSITION[mUiConfig.getTextAlignFlag()]);
            tv.setTextSize(mUiConfig.getItemTextSize());

            frameLayout.setLayoutParams(flp);
            frameLayout.addView(tv);
            frameLayout.setPadding(2, 2, 2, 2);
            curDayLayout.setPadding(mUiConfig.getItemSideMargin(), 0, mUiConfig.getItemSideMargin(), 0);
            //add to current day view
            curDayLayout.addView(frameLayout);

            int sectionStart = bCourse.getSectionStart();
            int sectionContinue = bCourse.getSectionContinue();

            while (sectionContinue > 0) {
                map.put((sectionStart + sectionContinue - 1) + "/" + thisDay, 0);
                sectionContinue--;
            }

            final int finalItemPostion = itemPosition;
            final boolean tempIsCurWeek = isCurWeek;
            //单击监听
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickCourseItemListener == null) {
                        return;
                    }
                    mClickCourseItemListener.onClickItem(view, mDataConfig.getCourseList(), finalItemPostion, tempIsCurWeek);
                }
            });
            frameLayout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongClickCourseItemListener != null) {
                        mLongClickCourseItemListener.onClickItem(v, mDataConfig.getCourseList(), finalItemPostion, tempIsCurWeek);
                    }
                    return true;
                }
            });
        }

        //not show non cur week
        if (!mUiConfig.isShowNotCurWeekCourse()) {
            return;
        }

        for (int i = 0; i < mBcourses.size(); i++) {
            itemPosition = i;
            boolean isCurWeek;
            BCourse bCourse = mBcourses.get(i);
            if (bCourse == null) {
                continue;
            }
            //check the position
            if (bCourse.getSectionStart() + bCourse.getSectionContinue() > mUiConfig.getMaxSection() + 1) {
                bCourse.setSectionContinue(mUiConfig.getMaxSection() + 1 - bCourse.getSectionStart());
            }
            //is this week
            int thisDay = bCourse.getDay() - 1;
            RelativeLayout curDayLayout = layoutList.get(thisDay);
            isCurWeek = bCourse.getWeek().contains(mDataConfig.curWeek());
            //first to load curweek class
            if (isCurWeek) {
                continue;
            }
            //if this position has a course,skip it
            if (map.containsKey(bCourse.getSectionStart() + "/" + thisDay)) {
                continue;
            }

            RelativeLayout frameLayout = new RelativeLayout(getContext());
            TextView tv = new TextView(mContext);
            //course height = class continue * little section height + gap * padding
            int bCourseHeight = bCourse.getSectionContinue() * mUiConfig.getSectionHeight()
                    + (bCourse.getSectionContinue() - 1) * mUiConfig.getItemTopMargin();
            RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    bCourseHeight);
            flp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            //class gap
            int topMargin = (bCourse.getSectionStart() - 1) * mUiConfig.getSectionHeight()
                    + bCourse.getSectionStart() * mUiConfig.getItemTopMargin();
            flp.setMargins(0, topMargin, 0, 0);
            //is non cur week
            frameLayout.setBackground(Drawables.getDrawable(mUiConfig.getItemNotCurWeekCourseColor(), 20, 3, mUiConfig.getColorUI()));
            tv.setTextColor(Color.BLACK);
            tv.setText(bCourse.getSimpleName(mUiConfig.getMaxHideCharLimit()) + "@" + bCourse.getPosition() + "[非本周]");
            tv.setLayoutParams(tlp);
            tv.setPadding(10, 10, 10, 10);
            tv.setGravity(UIConfig.POSITION[mUiConfig.getTextAlignFlag()]);
            tv.getPaint().setFakeBoldText(true);
            tv.setTextSize(mUiConfig.getItemTextSize());
            frameLayout.setLayoutParams(flp);
            frameLayout.addView(tv);
            frameLayout.setPadding(2, 2, 2, 2);
            curDayLayout.setPadding(mUiConfig.getItemSideMargin(), 0, mUiConfig.getItemSideMargin(), 0);
            curDayLayout.addView(frameLayout);
            final int finalItemPostion = itemPosition;
            final boolean tempIsCurWeek = isCurWeek;
            //单击监听
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickCourseItemListener == null) {
                        return;
                    }
                    mClickCourseItemListener.onClickItem(view, mDataConfig.getCourseList(), finalItemPostion, tempIsCurWeek);
                }
            });

            frameLayout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }

    }

    /*初始化*/
    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.main_course_view, this);
        mWeekView = findViewById(R.id.weekView);
        mSectionView = findViewById(R.id.sectionView);
        weekPanel1 = findViewById(R.id.weekPanel_1);
        weekPanel2 = findViewById(R.id.weekPanel_2);
        weekPanel3 = findViewById(R.id.weekPanel_3);
        weekPanel4 = findViewById(R.id.weekPanel_4);
        weekPanel5 = findViewById(R.id.weekPanel_5);
        weekPanel6 = findViewById(R.id.weekPanel_6);
        weekPanel7 = findViewById(R.id.weekPanel_7);
        if (mWeekView == null || mSectionView == null
                || weekPanel1 == null || weekPanel2 == null
                || weekPanel3 == null || weekPanel4 == null
                || weekPanel5 == null || weekPanel6 == null
                || weekPanel7 == null
        ) {
            throw new NullPointerException("childView is null");
        }
        layoutList.add(weekPanel1);
        layoutList.add(weekPanel2);
        layoutList.add(weekPanel3);
        layoutList.add(weekPanel4);
        layoutList.add(weekPanel5);
        layoutList.add(weekPanel6);
        layoutList.add(weekPanel7);
    }


    //加载侧边栏
    private void updateSectionView() {
        isSectionViewInit = false;
        //初始化上课节数视图
        initSectionView(mUiConfig.getMaxSection());
        isSectionViewInit = true;
    }

    //加载顶部栏
    private void updateWeekView() {
        //初始化课表上课日
        initWeekView(mUiConfig.getMaxClassDay());
    }

    /*更新数据*/
    private void updateData() {
        initCourseItemView();
    }


    public OnClickCourseItemListener getLongClickCourseItemListener() {
        return mLongClickCourseItemListener;
    }

    public void setLongClickCourseItemListener(OnClickCourseItemListener mLongClickCourseItemListener) {
        this.mLongClickCourseItemListener = mLongClickCourseItemListener;
    }
}
