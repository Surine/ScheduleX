package cn.surine.schedulex.base.http;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import cn.surine.schedulex.base.controller.AbstractSingleTon;
import cn.surine.schedulex.base.utils.Logs;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 12:42
 */
public class OkNet {
    private static OkHttpClient mOkHttpClient;
    private static ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Logs.d("WebRequest = " + message));

    public static AbstractSingleTon<OkNet> abt = new AbstractSingleTon<OkNet>() {
        @Override
        protected OkNet newObj(Bundle bundle) {
            return new OkNet();
        }
    };

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


//    object OkNet {
//        val cookieStore: ConcurrentHashMap<String, List<Cookie>> = ConcurrentHashMap()
//        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
//        .connectTimeout(5, TimeUnit.SECONDS)
//        .addInterceptor(HttpLoggingInterceptor {
//        Log.d("WebRequest", it)
//        //6月24日：fix 使用kotlin重写方法的时候 IDE可能会自动提示为List -> mutablelist
//        //这样导致编译无法通过，但在此场景下我们并不需要可变列表，因此改回List就可以通过了，
//        //这应该属于IDE问题
//        }).cookieJar(object : CookieJar {
//        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
//        cookieStore[url.toString()] = cookies
//        }
//
//        override fun loadForRequest(url: HttpUrl): List<Cookie> {
//        return cookieStore[url.toString()] ?: arrayListOf()
//        }
//        })
//        .build()
//        }