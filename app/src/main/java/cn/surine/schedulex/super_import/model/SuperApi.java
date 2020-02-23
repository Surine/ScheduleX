package cn.surine.schedulex.super_import.model;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SuperApi {

    /**
     * 获取课程列表
     * @param map
     * */
    @FormUrlEncoded
    @POST("http://120.55.151.61/V2/Course/getCourseTableFromServer.action")
    Flowable<SuperBaseModel<SuperCourseList>> getCourseList(@FieldMap Map<String, Object> map);


    /**
     * 登录
     * @param map
     * */
    @FormUrlEncoded
    @POST("http://120.55.151.61/V2/StudentSkip/loginCheckV4.action")
    Flowable<SuperBaseModel<User>> login(@FieldMap Map<String, Object> map);
}
