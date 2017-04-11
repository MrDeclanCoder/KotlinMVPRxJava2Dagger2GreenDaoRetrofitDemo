package com.dch.test;

import android.content.Context;
import android.support.annotation.NonNull;

import com.dch.test.repository.ArticalRepository;
import com.dch.test.repository.local.ArticalLocalDataSource;
import com.dch.test.repository.remote.ArticalRemoteDataSource;

/**
 * 作者：Dch on 2017/4/10 18:22
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class Injection {

    public static ArticalRepository provideArticalRepository(@NonNull Context context){
        return ArticalRepository.getInstance(ArticalLocalDataSource.getInstance(context), ArticalRemoteDataSource.getInstance());
    }
}
