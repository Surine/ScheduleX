package cn.surine.schedulex.data.network;

import cn.surine.schedulex.data.entity.CourseList;
import cn.surine.schedulex.data.entity.VmResultString;
import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("wx/xs/courseSelectMangement/queryCourseList")
    Flowable<CourseList> getSchedule(@Field("selectWeek") int i);

    @FormUrlEncoded
    @POST("wx/xs/personalManagement/xjbView")
    Call getUserInfo();

    @FormUrlEncoded
    @POST("wx/login/ckUser")
    Flowable<VmResultString> login(@Field("userid") String str, @Field("password") String str2);
}
