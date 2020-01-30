package cn.surine.schedulex.data.network;

import cn.surine.schedulex.base.http.Retrofits;

/**
 * Intro：sLoader
 * 由于所有订阅转交给BaseRepository处理，
 * 所以这里的Loader职责比较简单，主要是用来返回service
 * 如果是多个service可以维护多个成员变量
 * @author sunliwei
 * @date 2020-01-17 10:22
 */
public class Loader {

    private static Loader sLoader;
    private Api mService;

    public synchronized static Loader getInstance() {
        if (sLoader == null) {
            sLoader = new Loader();
        }
        return sLoader;
    }

    private Loader() {
        mService = Retrofits.getService(Api.class);
    }

    public Api getService() {
        return mService;
    }
}
