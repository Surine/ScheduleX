package cn.surine.schedulex.ui.schedule_data_fetch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;
import java.util.UUID;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.Files;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Objs;
import cn.surine.schedulex.base.utils.Others;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Spans;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentDataFetchBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_data_fetch.file.FileParser;
import cn.surine.schedulex.ui.schedule_data_fetch.file.FileParserFactory;
import cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-23 20:54
 */
public class ScheduleDataFetchFragment extends BaseBindingFragment<FragmentDataFetchBinding> {

    private static final int JSON_REQUEST_CODE = 1001;
    ScheduleViewModel scheduleViewModel;
    private CourseViewModel courseViewModel;
    private String scheduleName;

    @Override
    public int layoutId() {
        return R.layout.fragment_data_fetch;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onInit(FragmentDataFetchBinding t) {
        Class[] classes = new Class[]{ScheduleRepository.class};
        Object[] args = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classes, args)).get(ScheduleViewModel.class);


        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);

        //获取课表名
        Bundle bundle = new Bundle();
        scheduleName = Objs.notNull(getArguments()) ? getArguments().getString(ScheduleInitFragment.SCHEDULE_NAME) : "UNKNOWN";
        bundle.putString(ScheduleInitFragment.SCHEDULE_NAME, scheduleName);
        t.loginJw.setOnClickListener(v -> Navigations.open(ScheduleDataFetchFragment.this, R.id.action_dataFetchFragment_to_loginFragment, bundle));
        t.fromSuperCn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_dataFetchFragment_to_superLoginFragment, bundle));
        t.skip.setOnClickListener(v -> {
            Prefs.save(Constants.CUR_SCHEDULE, scheduleViewModel.addSchedule(scheduleName, 24, 1, Schedule.IMPORT_WAY.ADD));
            Navigations.open(ScheduleDataFetchFragment.this, R.id.action_dataFetchFragment_to_scheduleFragment);
        });
        t.adaptationPlan.setOnClickListener(v -> CommonDialogs.getCommonDialog(activity(), "适配计划", "一般的，通过超级课程表登录即可导入您相应的课程数据，" +
                "如果超级课程表暂时无法获取您的课程数据，您可以通过文件导入或者手动输入添加课程。\n\n为了更方便的导入，您可以申请适配您的教务处网站" +
                "，但由于这种适配开发不便，所以开发过程中需要您的鼎力支持。\n\n现阶段，适配请加QQ群咨询：686976115 点击确定将为您复制QQ号并跳转，感谢您为ScheduleX的发展贡献力量！", () -> Others.startQQ(activity(), "686976115"), null).show());

        t.fromFile.setOnClickListener(v -> {
            RxPermissions rxPermissions = new RxPermissions(activity());
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                if (aBoolean) {
                    showImportDialog();
                } else {
                    Toasts.toast(getString(R.string.permission_is_denied));
                }
            });
        });
    }

    /**
     * 展示导入对话框
     */
    private void showImportDialog() {
        if (Prefs.getBoolean(Constants.FILE_SELECTOR_DONT_TIP, false)) {
            importFile();
            return;
        }
        CommonDialogs.getBaseConfig(activity(), Uis.inflate(activity(), R.layout.view_import_file), (view, dialog) -> {
            TextView tJson = view.findViewById(R.id.t_json);
            TextView tExcel = view.findViewById(R.id.t_excel);
            TextView tCsv = view.findViewById(R.id.t_csv);
            Button vBok = view.findViewById(R.id.b_ok);
            CheckBox vDontTip = view.findViewById(R.id.c_dont_tip);

            String sJson = tJson.getText().toString() + "\n点击下载模板";
            tJson.setText(Spans.with(sJson).size(13, tJson.getText().toString().length(), sJson.length()).color(App.context.getResources().getColor(R.color.blue), tJson.getText().toString().length(), sJson.length()).toSpannable());

            String sExcel = tExcel.getText().toString() + "\n点击下载模板";
            tExcel.setText(Spans.with(sExcel).size(13, tExcel.getText().toString().length(), sExcel.length()).color(App.context.getResources().getColor(R.color.blue), tExcel.getText().toString().length(), sExcel.length()).toSpannable());

            String sCsv = tCsv.getText().toString() + "\n点击下载模板";
            tCsv.setText(Spans.with(sCsv).size(13, tCsv.getText().toString().length(), sCsv.length()).color(App.context.getResources().getColor(R.color.blue), tCsv.getText().toString().length(), sCsv.length()).toSpannable());

            tJson.setOnClickListener(v -> {
                Toasts.toast("请在本页面查看Json格式");
                Others.openUrl("https://github.com/surine/ScheduleX");
            });
            tCsv.setOnClickListener(v -> {
                Toasts.toast("请在本页面查看Csv格式");
                Others.openUrl("https://github.com/surine/ScheduleX");
            });
            tExcel.setOnClickListener(v -> Toasts.toast("难产啦！再等等吧。"));

            vBok.setOnClickListener(v -> {
                if (vDontTip.isChecked()) {
                    Prefs.save(Constants.FILE_SELECTOR_DONT_TIP, true);
                }
                importFile();
                dialog.dismiss();
            });

        });
    }


    /**
     * 导入
     */
    private void importFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        this.startActivityForResult(intent, JSON_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // 用户未选择任何文件，直接返回
            return;
        }
        if (requestCode == JSON_REQUEST_CODE) {
            try {
                Uri uri = data.getData();
                loadData(Files.getFilePath(activity(), uri));
            } catch (Exception e) {
                CrashReport.postCatchedException(e);
            }
        }

    }

    /**
     * 加载数据
     *
     * @param path
     */
    private void loadData(String path) {
        String[] data = path.split("\\.");
        FileParser parser = FileParserFactory.abt.getInstance().get(data[data.length - 1]);
        List<Course> list = parser.parse(path);
        if (list == null || list.size() == 0) {
            Toasts.toast("无数据，请检查资源格式是否正确");
            return;
        }
        //开始生成列表
        long id;
        Course[] courses = new Course[list.size()];
        Prefs.save(Constants.CUR_SCHEDULE, (id = scheduleViewModel.addSchedule(scheduleName, 24, 1, Schedule.IMPORT_WAY.JSON)));
        for (int i = 0; i < list.size(); i++) {
            Course course = list.get(i);
            course.scheduleId = id;
            course.id = course.scheduleId + "@"+ UUID.randomUUID()+System.currentTimeMillis();
            courses[i] = course;
        }
        courseViewModel.insert(courses);
        Toasts.toast(getString(R.string.handle_success));
        Navigations.open(ScheduleDataFetchFragment.this, R.id.action_dataFetchFragment_to_scheduleFragment);
    }
}
