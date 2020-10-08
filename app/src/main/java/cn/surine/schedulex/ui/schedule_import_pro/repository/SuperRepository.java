package cn.surine.schedulex.ui.schedule_import_pro.repository;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import cn.surine.schedulex.base.controller.AbstractSingleTon;
import cn.surine.schedulex.base.controller.BaseRepository;
import cn.surine.schedulex.ui.schedule_import_pro.model.super_model.SuperBaseModel;
import cn.surine.schedulex.ui.schedule_import_pro.model.super_model.SuperCourseList;
import cn.surine.schedulex.ui.schedule_import_pro.model.super_model.SuperLoader;
import cn.surine.schedulex.ui.schedule_import_pro.model.super_model.User;
import cn.surine.schedulex.ui.schedule_import_pro.util.SuperUtil;
import io.reactivex.Flowable;

public class SuperRepository extends BaseRepository {
    public static AbstractSingleTon<SuperRepository> abt = new AbstractSingleTon<SuperRepository>() {
        @Override
        public SuperRepository newObj(Bundle bundle) {
            return new SuperRepository();
        }
    };

    public Flowable<SuperBaseModel<User>> loginSuper(String str, String str2) {
        Map<String, Object> hashMap = new HashMap(9);
        hashMap.put("account", SuperUtil.encrypt(str));
        hashMap.put("password", SuperUtil.encrypt(str2));
        hashMap.put("platform", 1);
        hashMap.put("phoneVersion", VERSION.SDK_INT);
        hashMap.put("phoneBrand", Build.BRAND);
        hashMap.put("versionNumber", "9.4.1");
        hashMap.put("phoneModel", Build.MODEL);
        hashMap.put("updateInfo", Boolean.FALSE);
        hashMap.put("channel", "ppMarket");
        return SuperLoader.getInstance().getService().login(hashMap).compose(schedulerHelper());
    }

    public Flowable<SuperBaseModel<SuperCourseList>> getCourseList(int i, int i2) {
        Map<String, Object> hashMap = new HashMap(7);
        hashMap.put("beginYear", i);
        hashMap.put("term", i2);
        hashMap.put("platform", 1);
        hashMap.put("phoneVersion", VERSION.SDK_INT);
        hashMap.put("phoneBrand", Build.BRAND);
        hashMap.put("versionNumber", "9.4.1");
        hashMap.put("phoneModel", Build.MODEL);
        return SuperLoader.getInstance().getService().getCourseList(hashMap).compose(schedulerHelper());
    }
}
