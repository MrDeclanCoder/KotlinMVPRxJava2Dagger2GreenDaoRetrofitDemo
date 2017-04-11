package com.dch.test.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * 作者：Dch on 2017/4/10 17:25
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public interface ArticalDataSource {

    interface LoadArticalCallback {
        void onArticalLoaded(ArrayList<Artical> list);
        void onDataNotAvailable();
    }

    void getArticalsData(@NonNull LoadArticalCallback callback);
}
