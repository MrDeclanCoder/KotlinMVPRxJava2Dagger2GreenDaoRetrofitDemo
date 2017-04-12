package com.dch.test.rx.helper;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * 作者：Dch on 2017/4/12 11:53
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class RxSchedulers {
    public static <T> ObservableTransformer<T,T> io_main(){

        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
