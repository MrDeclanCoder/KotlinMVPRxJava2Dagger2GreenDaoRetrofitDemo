package com.dch.test.manager;

import android.os.Environment;

import com.dch.test.repository.remote.apistores.CsdnApiService;
import com.dch.test.repository.remote.apistores.GankApiService;
import com.dch.test.util.NetUtil;
import com.dch.test.util.convertfactory.StringConverterFactory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * 作者：Dch on 2017/4/11 11:37
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class RetrofitManager {

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    private Retrofit mStringResponseRetrofit;
    private Retrofit mJsonResponseRetrofit;
    private OkHttpClient sOkHttpClient;
    private static RetrofitManager mRetrofitManager;

    private RetrofitManager() {
        initRetrofit();
        initJacksonRetrofit();
    }

    public static RetrofitManager getInstance() {
        //懒汉式
        if (null == mRetrofitManager) {
            synchronized (RetrofitManager.class) {
                if (null == mRetrofitManager) {
                    mRetrofitManager = new RetrofitManager();
                }
            }
        }
        return mRetrofitManager;
    }

    private void initRetrofit() {
        mStringResponseRetrofit = new Retrofit.Builder()
                .baseUrl(CsdnApiService.base_url)
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();

    }

    private void initJacksonRetrofit() {
        mJsonResponseRetrofit = new Retrofit.Builder()
                .baseUrl(GankApiService.base_url)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();

    }

    private OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                Cache cache = new Cache(new File(Environment.getExternalStorageDirectory() + "/cache"), 1024 * 1024 * 100);
                if (sOkHttpClient == null) {
                    sOkHttpClient = new OkHttpClient.Builder().cache(cache)
                            .connectTimeout(6, TimeUnit.SECONDS)
                            .readTimeout(6, TimeUnit.SECONDS)
                            .writeTimeout(6, TimeUnit.SECONDS)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mLoggingInterceptor)
                            .build();
                }
            }
        }
        return sOkHttpClient;
    }
    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetUtil.isNetworkAvailable()) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .addHeader("Content-Type","application/json;charset=UTF-8")
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .addHeader("Content-Type","application/json;charset=UTF-8")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    private final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            return response;
        }
    };

    public CsdnApiService createCsdnApiService() {
        return mStringResponseRetrofit.create(CsdnApiService.class);
    }

    public GankApiService createGankApiService() {
        return mJsonResponseRetrofit.create(GankApiService.class);
    }
}
