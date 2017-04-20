package com.dch.test.contract.presenter;

import android.support.v7.app.AppCompatActivity;

import com.dch.test.base.BaseView;
import com.dch.test.contract.AndroidContract;
import com.dch.test.di.ActivityScope;
import com.dch.test.repository.ArticalDataSource;
import com.dch.test.repository.ArticalRepository;
import com.dch.test.repository.entity.GankEntity;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 作者：dch on 2017/4/10 16:32
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@ActivityScope
public class AndroidPresenter implements AndroidContract.Presenter {
    private final AndroidContract.AndroidView androidView;
    private final ArticalRepository mArticalRepository;

    @Inject
    public AndroidPresenter(AndroidContract.AndroidView view, ArticalRepository articalRepository) {
        androidView = checkNotNull(view, "view不能为空");
        mArticalRepository = checkNotNull(articalRepository, "androidRepository不能为空");
        androidView.setPresenter(this);
    }

    @Override
    public void start() {
        getAndroidData();
    }

    @Override
    public void getAndroidData() {
        mArticalRepository.getAndroidData(new ArticalDataSource.GankCallback() {
            @Override
            public void onGankdataLoaded(GankEntity entity) {
                androidView.showAndroidDailyList(entity);
            }

            @Override
            public void onDataNotAvailable(Throwable throwable) {
                androidView.showError(throwable);
            }
        });
    }
}
