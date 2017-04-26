package com.dch.test.ui;

import android.animation.ObjectAnimator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.util.StatusBarUtils;

import butterknife.BindView;

/**
 * 作者：Dch on 2017/4/17 17:02
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class DetailActivity extends BaseActivity {
    @BindView(R.id.iv_gank_detail)
    ImageView iv_gank_detail;
    @BindView(R.id.wb_detail)
    WebView mWebView;
    @BindView(R.id.toolbar_deitail)
    Toolbar toolbar_deitail;
    @BindView(R.id.pb_detail)
    ProgressBar pb_detail;
    private ObjectAnimator alphaAnimation;

    @Override
    protected void initView() {
        setSupportActionBar(toolbar_deitail);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        toolbar_deitail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alphaAnimation = ObjectAnimator.ofFloat(pb_detail,"alpha",1.0f,0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
    }


    @Override
    protected void initData() {
        String imgurl = getIntent().getStringExtra("imgurl");
        Glide.with(this).load(imgurl).placeholder(R.drawable.guide4).into(iv_gank_detail);
        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
        toolbar_deitail.setTitle(mWebView.getTitle());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress>98){
                    alphaAnimation.start();
                    pb_detail.setVisibility(View.GONE);
                } else {
                    pb_detail.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_details;
    }
}
