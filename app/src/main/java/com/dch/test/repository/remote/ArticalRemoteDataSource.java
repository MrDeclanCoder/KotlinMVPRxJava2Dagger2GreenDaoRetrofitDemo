package com.dch.test.repository.remote;

import android.support.annotation.NonNull;

import com.dch.test.repository.ArticalDataSource;

/**
 * 作者：Dch on 2017/4/10 18:20
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class ArticalRemoteDataSource implements ArticalDataSource {

    private static ArticalRemoteDataSource INSTANCE;

    public static ArticalRemoteDataSource getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ArticalRemoteDataSource();
        }
        return INSTANCE;
    }

    private ArticalRemoteDataSource() {
    }

    @Override
    public void getArticalsData(@NonNull LoadArticalCallback callback) {

    }
}
