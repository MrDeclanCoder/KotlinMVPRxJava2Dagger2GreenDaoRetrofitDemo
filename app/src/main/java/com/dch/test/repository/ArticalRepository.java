package com.dch.test.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 作者：Dch on 2017/4/10 17:23
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class ArticalRepository implements ArticalDataSource {

    private static ArticalRepository INSTANCE;
    private ArticalDataSource articalLocalDataSource;
    private ArticalDataSource articalRemoteDataSource;

    private ArticalRepository(@NonNull ArticalDataSource articalLocalDataSource,
                              @NonNull ArticalDataSource articalRemoteDataSource) {
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
        /*articalLocalDataSource.getArticalsData(new LoadArticalCallback() {
            @Override
            public void onArticalLoaded(ArrayList<Artical> list) {

            }

            @Override
            public void onDataNotAvailable() {

            }
        });*/
        //数据库缓存没有或者过期则从远程(网络)获取
        getArticalDataFromRemote(callback);
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
