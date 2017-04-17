package com.dch.test.repository.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.dch.test.repository.ArticalDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 作者：Dch on 2017/4/10 17:24
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class ArticalLocalDataSource implements ArticalDataSource {

    private static ArticalLocalDataSource INSTANCE;

    public static ArticalLocalDataSource getInstance(@NonNull Context context) {
        if (null == INSTANCE) {
            INSTANCE = new ArticalLocalDataSource(context);
        }
        return INSTANCE;
    }

    private ArticalLocalDataSource(@NonNull Context context){
        checkNotNull(context);
    }

    @Override
    public void getArticalsData(@NonNull LoadArticalCallback callback) {

    }

    @Override
    public void getMeiziData(@NonNull GankCallback callback) {

    }

    @Override
    public void getAndroidData(@NonNull GankCallback callback) {

    }
}
