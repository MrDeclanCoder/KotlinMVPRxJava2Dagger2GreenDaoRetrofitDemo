package com.dch.test.repository.remote.apistores;

import com.dch.test.repository.entity.GankEntity;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * 作者：Dch on 2017/4/17 14:11
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public interface GankApiService {

    public String base_url = "http://gank.io/api/data/";
    public String android_url = "http://gank.io/api/data/";


    @GET("{type}/{count}/{pageIndex}")
    Flowable<GankEntity> getDailyMeiziData(@Path("type") String type,@Path("count") int count,@Path("pageIndex") int pageIndex);

    @GET("{type}/{count}/{pageIndex}")
    Flowable<GankEntity> getDailyAndroidData(@Path("type") String type,@Path("count") int count,@Path("pageIndex") int pageIndex);
}
