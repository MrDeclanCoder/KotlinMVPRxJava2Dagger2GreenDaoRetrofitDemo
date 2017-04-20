package com.dch.test.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Handler;
import android.transition.Explode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.util.StatusBarUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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

    private Handler handler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setupWindowAnimations();
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            finish();
        }
    };

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
        handler = new Handler();
        handler.postDelayed(runnable, 8500);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView_splash, "alpha", 1f, 0.5f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView_splash, "scaleX", 1f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView_splash, "scaleY", 1f, 1.1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(6000);
        animatorSet.play(alpha).with(scaleX).with(scaleY);
        animatorSet.start();

        long count = 3;
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long i) throws Exception {
                        return count - i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {

                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Long aLong) {
                        textView2.setText(String.valueOf(aLong));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        textView2.setText("跳过");
                        textView2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handler.removeCallbacks(runnable);
                                setupWindowAnimations();
                                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                                finish();
                            }
                        });
                    }
                });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }
}
