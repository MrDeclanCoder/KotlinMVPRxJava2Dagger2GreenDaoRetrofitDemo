package com.dch.test.util;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 作者：Dch on 2017/4/13 12:10
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class RxBus<T> {
    private Flowable<T> flowable;
    private FlowableEmitter<T> emmiter;
    private static RxBus instance;

    private RxBus() {
        flowable = Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<T> e) throws Exception {
                emmiter = e;
            }
        }, BackpressureStrategy.ERROR);
    }

    public static RxBus getInstance() {
        if (null == instance) {
            synchronized (RxBus.class) {
                if (null == instance) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    //主线程接收消息
    public void registSubject(final CallBack<T> callBack) {
        flowable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(@NonNull T o) {
                        try {
                            callBack.onNext(o);
                        } catch (Exception e){
                            callBack.onError(e);
                        }
                    }
                });

    }

    public void post(T obj) {
        if (emmiter != null)
            emmiter.onNext(obj);
    }

    public interface CallBack<T> {
        void onNext(T s);

        void onError(Exception t);
    }

}
