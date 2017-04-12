package com.dch.test.manager;

import android.os.Environment;

import com.dch.test.BuildConfig;
import com.dch.test.repository.remote.ApiService;
import com.dch.test.util.convertfactory.StringConverterFactory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * 作者：Dch on 2017/4/11 11:37
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class RetrofitManager {
    private Retrofit mRetrofit;
    private static RetrofitManager mRetrofitManager;

    private RetrofitManager(){
        initRetrofit();
    }

    public static RetrofitManager getInstance(){
        //懒汉式
        if (null == mRetrofitManager){
            synchronized (RetrofitManager.class){
                if (null == mRetrofitManager){
                    mRetrofitManager = new RetrofitManager();
                }
            }
        }
        return mRetrofitManager;
    }

    private void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10000, TimeUnit.SECONDS);
        builder.writeTimeout(5000,TimeUnit.SECONDS);
        builder.readTimeout(5000,TimeUnit.SECONDS);

//        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            builder.addInterceptor(interceptor);
//        }

        Cache cache = new Cache(new File(Environment.getExternalStorageDirectory()+"/cache"),50000);
        builder.cache(cache);
        builder.retryOnConnectionFailure(false);
        OkHttpClient okHttpClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiService.base_url)
//                .addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public <T> T createReq(Class<T> reqServer){
        return mRetrofit.create(reqServer);
    }

    public ApiService createApiService(){
        return mRetrofit.create(ApiService.class);
    }
}
