package cn.surine.schedulex.data.network;

import java.util.List;

import cn.surine.schedulex.data.entity.CourseList;
import cn.surine.schedulex.data.entity.VmResultString;
import cn.surine.schedulex.third_parse.JwInfo;
import io.reactivex.Flowable;
import kotlinx.coroutines.Deferred;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    /**
     * 获取适配列表
     * */
    @GET("https://surinex.coding.net/p/schedulex/d/schedulex/git/raw/master/schools.json")
    Deferred<List<JwInfo>> getAdapterList();

}
