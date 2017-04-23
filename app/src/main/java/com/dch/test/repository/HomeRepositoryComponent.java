package com.dch.test.repository;

import com.dch.test.di.app.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dch on 2017/4/23.
 */

@Singleton
@Component(modules = {ArticalRepositoryModule.class, AppModule.class})
public interface HomeRepositoryComponent {

    ArticalRepository getArticalRepository();
}
