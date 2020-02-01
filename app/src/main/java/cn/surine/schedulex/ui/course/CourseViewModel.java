package cn.surine.schedulex.ui.course;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.http.BaseHttpSubscriber;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.CourseList;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-19 22:12
 */
public class CourseViewModel extends ViewModel {

    private CourseRepository mCourseRepository;
    public MutableLiveData<List<Course>> mMutableCourses;
    public MutableLiveData<Integer> getCourseStatus = new MutableLiveData<>();
    public static final int START = 1;
    public static final int SUCCESS = 2;
    public static final int FAIL = 3;


    public MutableLiveData<Integer> totalWeek = new MutableLiveData<>();
    public MutableLiveData<Integer> nowWeek = new MutableLiveData<>();


    private HashMap<String,String> colorHashMap = new HashMap<>();

    public CourseViewModel(CourseRepository courseRepository) {
        this.mCourseRepository = courseRepository;
        if (mMutableCourses == null) {
            mMutableCourses = new MutableLiveData<>();
            mMutableCourses.setValue(new ArrayList<>());
        } else {
            if (mMutableCourses.getValue() != null) {
                mMutableCourses.getValue().clear();
            }
        }
    }


    public void getCourseByNet() {
        getCourseStatus.setValue(START);
        mCourseRepository.getCourseByNet(-1).subscribe(new BaseHttpSubscriber<CourseList>() {
            @Override
            public void onSuccess(MutableLiveData<CourseList> vm) {
                int tWeek = 0,nWeek = 0;
                if (vm.getValue() != null) {
                    tWeek = vm.getValue().weeks;
                    nWeek = vm.getValue().nowWeek;
                }
                totalWeek.setValue(tWeek);
                nowWeek.setValue(nWeek);
                getWeekCourseByNet(tWeek);
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                getCourseStatus.setValue(FAIL);
            }
        });
    }

    /**
     * 单周获取后缓存
     */
    private void getWeekCourseByNet(int totalWeek) {
        mCourseRepository.getCourseByNet(totalWeek).subscribe(new BaseHttpSubscriber<CourseList>() {
            @Override
            public void onSuccess(MutableLiveData<CourseList> vm) {
                List<Course> mCourseList = mMutableCourses.getValue();

                //添加数据
                if (vm.getValue().courseList != null) {
                    for (Course course : vm.getValue().courseList) {
                        course.belongsToWeek = totalWeek;  //对所属周赋值
                        if(colorHashMap.containsKey(course.coureNumber)){
                            course.color = colorHashMap.get(course.coureNumber);
                        }else{
                            course.color = Constants.COLOR_1[new Random(System.currentTimeMillis()).nextInt(10)];
                            colorHashMap.put(course.coureNumber,course.color);
                        }
                        mCourseList.add(course);
                    }
                }

                mMutableCourses.setValue(mCourseList);
                if (totalWeek > 1) {
                    getWeekCourseByNet(totalWeek - 1);
                } else {
                    getCourseStatus.setValue(SUCCESS);
                    colorHashMap.clear();
                }
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                getCourseStatus.setValue(FAIL);
                colorHashMap.clear();
            }
        });
    }


    public void saveCourseByDb(List<Course> courseList) {
        mCourseRepository.clearCourseByDb();
        mCourseRepository.saveCourseByDb(courseList);
    }




    public List<Course> queryCourseByWeek(int week, int scheduleId) {
        return mCourseRepository.queryCourseByWeek(week,scheduleId);
    }

    public void deleteCourseByScheduleId(long scheduleId) {
        mCourseRepository.deleteCourseByScheduleId(scheduleId);
    }
}
