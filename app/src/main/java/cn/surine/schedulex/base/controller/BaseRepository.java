package cn.surine.schedulex.base.controller;

import cn.surine.schedulex.base.database.BaseAppDatabase;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Intro：
 * 这是数据源的配置，这里数据库和网络都是公用的控制器，如果需要分离需要在子类中实现各自的数据源
 * <p>
 * 注意：按理说上层对下层的引用需要传参传递过去，推测其中一个原因防止new多份而占用内存
 * 但这里为了方便就直接在上层写下层的引用。
 *
 * @author sunliwei
 * @date 2020-01-17 15:50
 */
public class BaseRepository {
    /**
     * 数据库管理器
     */
    protected BaseAppDatabase appDatabase = BaseAppDatabase.getInstance(App.context);


    /**
     * Rx线程控制工具
     */
    protected <T> FlowableTransformer<T, T> schedulerHelper() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
