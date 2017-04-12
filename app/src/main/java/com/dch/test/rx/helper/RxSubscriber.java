package com.dch.test.rx.helper;

import com.dch.test.repository.remote.HttpResult;

/**
 * 作者：Dch on 2017/4/12 12:32
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class RxSubscriber<T extends HttpResult> {
    private static final String TAG = "RxSubscriber";

    private static final String SOCKETTIMEOUTEXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";
    private static final String CONNECTEXCEPTION = "网络连接异常，请检查您的网络状态";
    private static final String UNKNOWNHOSTEXCEPTION = "网络异常，请检查您的网络状态";
}
