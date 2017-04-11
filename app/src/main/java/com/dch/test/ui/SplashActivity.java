package com.dch.test.ui;

import android.content.Intent;
import android.os.Handler;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;

/**
 * 作者：Dch on 2017/4/10 18:37
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class SplashActivity extends BaseActivity {

    private Handler handler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }
    };

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        handler = new Handler();
        handler.postDelayed(runnable, 2000);
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
