package com.dch.test.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Handler;
import android.transition.Explode;
import android.widget.ImageView;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.util.StatusBarUtils;

import butterknife.BindView;

/**
 * 作者：Dch on 2017/4/10 18:37
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.imageView_splash)
    ImageView imageView_splash;

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
        handler.postDelayed(runnable, 2500);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView_splash, "alpha", 1f, 0.5f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView_splash, "scaleX", 1f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView_splash, "scaleY", 1f, 1.1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(4000);
        animatorSet.play(alpha).with(scaleX).with(scaleY);
        animatorSet.start();
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
