package cn.surine.schedulex.data.network;

import java.util.List;

import cn.surine.schedulex.data.entity.Commons;
import cn.surine.schedulex.data.entity.CourseList;
import cn.surine.schedulex.data.entity.VmResultString;
import cn.surine.schedulex.third_parse.JwInfo;
import cn.surine.schedulex.ui.schedule_import_pro.model.RemoteUniversity;
import io.reactivex.Flowable;
import kotlinx.coroutines.Deferred;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

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

    /**
     * 获取适配列表
     */
    @GET
    Deferred<List<JwInfo>> getAdapterList(@Url String url);


    /**
     * 公共参数
     */
    @GET
    Deferred<Commons> getCommon(@Url String url);



    @GET("")
    Deferred<RemoteUniversity> getUniversityInfo(@Query("name") String name, @Query("code") String code);
}
