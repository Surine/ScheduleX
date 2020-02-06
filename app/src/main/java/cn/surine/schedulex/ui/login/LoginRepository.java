package cn.surine.schedulex.ui.login;

import android.os.Bundle;

import cn.surine.schedulex.base.controller.AbstractSingleTon;
import cn.surine.schedulex.base.controller.BaseRepository;
import cn.surine.schedulex.data.entity.VmResultString;
import cn.surine.schedulex.data.network.Loader;
import io.reactivex.Flowable;

/**
 * Intro：
 * 数据源
 *
 * @author sunliwei
 * @date 2020-01-17 10:54
 */
class LoginRepository extends BaseRepository {

    static AbstractSingleTon<LoginRepository> abt = new AbstractSingleTon<LoginRepository>() {
        @Override
        protected LoginRepository newObj(Bundle bundle) {
            return new LoginRepository();
        }
    };

    /**
     * 登录操作
     */
    Flowable<VmResultString> login(String account, String password) {
        return Loader.getInstance().getService().login(account, password).compose(schedulerHelper());
    }

}
