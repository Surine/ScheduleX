package cn.surine.schedulex.ui.schedule_import_pro.model.super_model;

import cn.surine.schedulex.base.http.Retrofits;

public class SuperLoader {
    private static SuperLoader sLoader;
    private SuperApi mService = Retrofits.getService(SuperApi.class);

    public static synchronized SuperLoader getInstance() {
        SuperLoader superLoader;
        synchronized (SuperLoader.class) {
            if (sLoader == null) {
                sLoader = new SuperLoader();
            }
            superLoader = sLoader;
        }
        return superLoader;
    }

    private SuperLoader() {
    }

    public SuperApi getService() {
        return this.mService;
    }
}
