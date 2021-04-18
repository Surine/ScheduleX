package cn.surine.schedulex.ui.course;

import androidx.annotation.Keep;
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

@Keep
public class CourseViewModel extends ViewModel {

    private CourseRepository mCourseRepository;
    public MutableLiveData<List<Course>> mMutableCourses;
    public MutableLiveData<Integer> getCourseStatus = new MutableLiveData<>();
    public static final int START = 1;
    public static final int SUCCESS = 2;
    public static final int FAIL = 3;


    public MutableLiveData<Integer> totalWeek = new MutableLiveData<>();
    public MutableLiveData<Integer> nowWeek = new MutableLiveData<>();


    private HashMap<String, String> colorHashMap = new HashMap<>();

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
                int tWeek = 0, nWeek = 0;
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
                        if (colorHashMap.containsKey(course.coureNumber)) {
                            course.color = colorHashMap.get(course.coureNumber);
                        } else {
                            course.color = Constants.COLOR_1[new Random(System.currentTimeMillis()).nextInt(Constants.COLOR_1.length)];
                            colorHashMap.put(course.coureNumber, course.color);
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


    public void saveCourseByDb(List<Course> courseList, long scheduleId) {
        //注意是清空当前课表的数据，不能清空全部数据库
        mCourseRepository.clearCourseByDb(scheduleId);
        mCourseRepository.saveCourseByDb(courseList);
    }


    //[week] -1 for all courses
    public List<Course> queryCourseByWeek(int week, int scheduleId) {
        return mCourseRepository.queryCourseByWeek(week, scheduleId);
    }

    public void deleteCourseByScheduleId(long scheduleId) {
        mCourseRepository.deleteCourseByScheduleId(scheduleId);
    }


    /**
     * 添加课程
     */
    public long insert(Course course) {
        return mCourseRepository.insert(course);
    }


    /**
     * 添加课程（多添加）
     */
    public void insert(Course... courses) {
        mCourseRepository.insert(courses);
    }


    /**
     * 根据id获取课程
     *
     * @param id
     */
    public Course getCourseById(String id) {
        return mCourseRepository.getCourseById(id);
    }

    public List<Course> getCourseByName(String name) {
        return mCourseRepository.getCourseByName(name);
    }


    /**
     * 更新课程
     */
    public void update(Course course) {
        mCourseRepository.update(course);
    }


    /**
     * 删除课程
     */
    public void deleteByCourseId(String id) {
        mCourseRepository.deleteByCourseId(id);
    }


    /**
     * 通过课表id获取课程
     */
    public List<Course> getCourseByScheduleId(int scheduleId) {
        return mCourseRepository.getCourseByScheduleId(scheduleId);
    }

    /**
     * 删除这节课程的week周
     */
    public void deleteCourseWeekByCourseId(String id, int week) {
        Course course = mCourseRepository.getCourseById(id);
        StringBuilder stringBuilder = new StringBuilder(course.classWeek);
        stringBuilder.replace(week - 1, week, "0");
        course.classWeek = stringBuilder.toString();
        if (course.classWeek.contains("1")) {
            mCourseRepository.update(course);
        } else {
            mCourseRepository.deleteByCourseId(id);
        }
    }
}
