package cn.surine.schedulex.base.http;

import androidx.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import cn.surine.schedulex.base.utils.Logs;
import cn.surine.schedulex.data.network.Api;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofits管理器
 *
 * @author sunliwei
 */
public class Retrofits {
    private Retrofit retrofit;

    static class Instance {
        static Retrofits retrofits = new Retrofits();
    }

    /**
     * 获取一个retrofits对象
     */
    private static Retrofits getInstance() {
        return Instance.retrofits;
    }

    private Retrofits() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Domin.DOMIN)
                .client(new OkNet().getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static <T> T getService(Class<T> clazz) {
        return Retrofits.getInstance().retrofit.create(clazz);
    }


    static class OkNet {
        private static OkHttpClient mOkHttpClient;
        private static ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Logs.d("WebRequest = " + message));

        private OkNet() {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
                            cookieStore.put(url.host(), cookies);
                        }

                        @NonNull
                        @Override
                        public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                            List<Cookie> cookies = cookieStore.get(url.host());
                            return cookies != null ? cookies : new ArrayList<>();
                        }
                    })
                    .build();
        }

        OkHttpClient getClient() {
            return mOkHttpClient;
        }

    }
}

