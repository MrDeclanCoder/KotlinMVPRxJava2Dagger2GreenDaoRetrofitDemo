package com.dch.test.repository.local;

import android.support.annotation.NonNull;

import com.dch.test.repository.ArticalDataSource;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 作者：Dch on 2017/4/10 17:24
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class ArticalLocalDataSource implements ArticalDataSource {

    private static ArticalLocalDataSource INSTANCE;

    @Inject
    public ArticalLocalDataSource () {
    }

    @Override
    public void getArticalsData(@NonNull LoadArticalCallback callback) {

    }

    @Override
    public void getMeiziData(@NonNull GankCallback callback, int pageNum, int pageSize) {

    }

    @Override
    public void getAndroidData(@NonNull GankCallback callback,int pageNum, int pageSize) {

    }
}
