package com.dch.test.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.transition.Explode;
import android.widget.ImageView;
import android.widget.TextView;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.util.Config;
import com.dch.test.util.SharePreferenceUtils;
import com.dch.test.util.StatusBarUtils;
import com.dch.test.widget.CountDownProgressBar;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 作者：Dch on 2017/4/10 18:37
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.imageview_splash)
    ImageView imageView_splash;

    @BindView(R.id.textView2)
    TextView textView2;

    @BindView(R.id.customprogressbar)
    CountDownProgressBar mProgressBar;

    private void judgeTurn() {
        if (SharePreferenceUtils.getPrefBoolean(getApplicationContext(), Config.APP_GUIDE, false)) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        } else {
            SharePreferenceUtils.setPrefBoolean(getApplicationContext(), Config.APP_GUIDE, true);
            startActivity(new Intent(SplashActivity.this, GuideActivity.class),ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        }
    }

    @Override
    protected void initData() {
    }

    @TargetApi(21)
    private void setupWindowAnimations() {
        Explode explode = new Explode();
        explode.setDuration(1000);
        getWindow().setExitTransition(explode);
    }

    @Override
    protected void initView() {
        StatusBarUtils.setImage(this);
        final ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView_splash, "alpha", 1f, 0.5f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView_splash, "scaleX", 1f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView_splash, "scaleY", 1f, 1.1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(6000);
        animatorSet.play(alpha).with(scaleX).with(scaleY);
        animatorSet.start();

        final Observer<Long> observer = new Observer<Long>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Long aLong) {
                if (mProgressBar != null) {
                    mProgressBar.start();
                } else {
                    disposable.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                if (mProgressBar != null) {
                    judgeTurn();
                } else {
                    disposable.dispose();
                }
            }
        };
        Observable<Long> longObservable = Observable.interval(400, 30, TimeUnit.MILLISECONDS)
                .take(100)
                .observeOn(AndroidSchedulers.mainThread());
        longObservable.subscribe(observer);
        mProgressBar.setOnClickListener(new CountDownProgressBar.OnClickListener() {
            @Override
            public void onClick() {
                judgeTurn();
            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onBackPressed() {
    }
}
