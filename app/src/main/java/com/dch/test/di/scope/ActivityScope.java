package com.dch.test.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 作者：Dch on 2017/4/20 14:46
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {}
