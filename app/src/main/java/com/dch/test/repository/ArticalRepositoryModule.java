package com.dch.test.repository;

import com.dch.test.repository.local.ArticalLocalDataSource;
import com.dch.test.repository.remote.ArticalRemoteDataSource;
import com.dch.test.repository.remote.Local;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by dch on 2017/4/23.
 */

@Module
abstract class ArticalRepositoryModule {

    @Singleton
    @Binds
    @Remote
    abstract ArticalDataSource provideRemoteArticalDataSource(ArticalRemoteDataSource remoteDataSource);

    @Singleton
    @Binds
    @Local
    abstract ArticalDataSource provideLocalArticalDataSource(ArticalLocalDataSource localDataSource);
}
