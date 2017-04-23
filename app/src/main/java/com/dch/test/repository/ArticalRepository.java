package com.dch.test.repository;

import android.support.annotation.NonNull;

import com.dch.test.repository.entity.GankEntity;
import com.dch.test.repository.remote.Local;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 作者：Dch on 2017/4/10 17:23
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com*/
@Singleton
public class ArticalRepository implements ArticalDataSource {

    private static ArticalRepository INSTANCE;
    private ArticalDataSource articalLocalDataSource;
    private ArticalDataSource articalRemoteDataSource;

    @Inject
    public ArticalRepository(@Local ArticalDataSource articalLocalDataSource,
                              @Remote ArticalDataSource articalRemoteDataSource) {
        this.articalLocalDataSource = checkNotNull(articalLocalDataSource);
        this.articalRemoteDataSource = checkNotNull(articalRemoteDataSource);
    }

    public static ArticalRepository getInstance(ArticalDataSource articalLocalDataSource, ArticalDataSource articalRemoteDataSource) {
        if (null == INSTANCE) {
            INSTANCE = new ArticalRepository(articalLocalDataSource, articalRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getArticalsData(@NonNull LoadArticalCallback callback) {
        checkNotNull(callback);

        //先从数据库判断缓存是否存在...
        /*articalLocalDataSource.getAndroidData(new LoadArticalCallback() {
            @Override
            public void onGankdataLoaded(ArrayList<Artical> list) {

            }

            @Override
            public void onDataNotAvailable() {

            }
        });*/
        //数据库缓存没有或者过期则从远程(网络)获取
        getArticalDataFromRemote(callback);
    }

    @Override
    public void getMeiziData(@NonNull GankCallback callback) {
        checkNotNull(callback);

        //先从数据库判断缓存是否存在...
        /*articalLocalDataSource.getAndroidData(new LoadArticalCallback() {
            @Override
            public void onGankdataLoaded(ArrayList<Artical> list) {

            }

            @Override
            public void onDataNotAvailable() {

            }
        });*/
        //数据库缓存没有或者过期则从远程(网络)获取
        getMeiziDataFromRemote(callback);
    }

    @Override
    public void getAndroidData(@NonNull GankCallback callback) {
        checkNotNull(callback);

        //先从数据库判断缓存是否存在...
        /*articalLocalDataSource.getAndroidData(new LoadArticalCallback() {
            @Override
            public void onGankdataLoaded(ArrayList<Artical> list) {

            }

            @Override
            public void onDataNotAvailable() {

            }
        });*/
        //数据库缓存没有或者过期则从远程(网络)获取
        getAndroidDataFromRemote(callback);
    }

    private void getAndroidDataFromRemote(final GankCallback callback) {
        articalRemoteDataSource.getAndroidData(new GankCallback() {
            @Override
            public void onGankdataLoaded(GankEntity entity) {
                callback.onGankdataLoaded(entity);
            }

            @Override
            public void onDataNotAvailable(Throwable throwable) {
                callback.onDataNotAvailable(throwable);
            }
        });
    }

    private void getMeiziDataFromRemote(final GankCallback callback) {
        articalRemoteDataSource.getMeiziData(new GankCallback() {
            @Override
            public void onGankdataLoaded(GankEntity entity) {
                callback.onGankdataLoaded(entity);
            }

            @Override
            public void onDataNotAvailable(Throwable throwable) {
                callback.onDataNotAvailable(throwable);
            }
        });
    }

    private void getArticalDataFromRemote(final LoadArticalCallback callback) {
        articalRemoteDataSource.getArticalsData(new LoadArticalCallback() {
            @Override
            public void onArticalLoaded(ArrayList<String> list) {
                callback.onArticalLoaded(list);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
