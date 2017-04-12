package com.dch.test.repository.remote;

import com.dch.test.repository.BaseEntity;
import com.dch.test.repository.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 作者：Dch on 2017/4/10 18:55
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public interface ApiService {
    public String base_url = "http://blog.csdn.net";
    public String list_url = "/coderder";

    @FormUrlEncoded
    @POST("getUser")
    Observable<BaseEntity<User>> getUser(@FieldMap Map<String, String> map);


    @GET(ApiService.list_url)
    Observable<String> getArticalList();
}
