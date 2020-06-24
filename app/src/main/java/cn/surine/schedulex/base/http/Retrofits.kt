package cn.surine.schedulex.base.http

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 11:33
 */
object Retrofits {
    private var retrofit: Retrofit = Retrofit.Builder().baseUrl("http://59.67.1.54/")
            .client(OkNet.abt.instance.client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @JvmStatic
    fun <T> getService(cls: Class<T>): T = retrofit.create(cls)

}