package com.dch.test.repository;

import android.support.annotation.NonNull;

import com.dch.test.repository.entity.GankEntity;

import java.util.ArrayList;

/**
 * 作者：Dch on 2017/4/10 17:25
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public interface ArticalDataSource {

    interface LoadArticalCallback {
        void onArticalLoaded(ArrayList<String> list);
        void onDataNotAvailable();
    }

    interface GankCallback {
        void onGankdataLoaded(GankEntity entity);
        void onDataNotAvailable(Throwable throwable);
    }

    void getArticalsData(@NonNull LoadArticalCallback callback);

    void getMeiziData(@NonNull GankCallback callback);

    void getAndroidData(@NonNull GankCallback callback,int pageNum, int pageSize);
}
