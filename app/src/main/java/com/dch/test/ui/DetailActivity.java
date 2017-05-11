package com.dch.test.ui;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

/**
 * 作者：Dch on 2017/4/17 17:02
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class DetailActivity extends BaseActivity {
    private ObjectAnimator alphaAnimation;

    @BindView(R.id.iv_gank_detail)
    ImageView iv_gank_detail;
    @BindView(R.id.wb_detail)
    WebView mWebView;
    @BindView(R.id.toolbar_deitail)
    Toolbar toolbar_deitail;
    @BindView(R.id.pb_detail)
    ProgressBar pb_detail;
    @BindView(R.id.collapsingtoolbarlayout)
    CollapsingToolbarLayout collapsingtoolbarlayout;
    @BindView(R.id.appbar_detail)
    AppBarLayout appbar_detail;

    @Override
    protected void initView() {
        setSupportActionBar(toolbar_deitail);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        collapsingtoolbarlayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
        collapsingtoolbarlayout.setExpandedTitleColor(Color.parseColor("#00ffffff"));
        toolbar_deitail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        alphaAnimation = ObjectAnimator.ofFloat(pb_detail, "alpha", 1.0f, 0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        webViewSetting();
    }


    @Override
    protected void initData() {
        String imgurl = getIntent().getStringExtra("imgurl");
        Glide.with(this).load(imgurl).placeholder(R.drawable.guide4).into(iv_gank_detail);
        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb_detail.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                alphaAnimation.start();
                pb_detail.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_details;
    }

    private void webViewSetting() {
        /* 设置支持Js,必须设置的,不然网页基本上不能看 */
        mWebView.getSettings().setJavaScriptEnabled(true);
        /* 设置缓存模式,我这里使用的默认,不做多讲解 */
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        /* 设置为true表示支持使用js打开新的窗口 */
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        /* 大部分网页需要自己保存一些数据,这个时候就的设置下面这个属性 */
        mWebView.getSettings().setDomStorageEnabled(true);
        /* 设置为使用webview推荐的窗口 */
        mWebView.getSettings().setUseWideViewPort(true);
        /* 设   置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
        mWebView.getSettings().setLoadWithOverviewMode(true);
        /* HTML5的地理位置服务,设置为true,启用地理定位 */
        mWebView.getSettings().setGeolocationEnabled(true);
        /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
        mWebView.getSettings().setBuiltInZoomControls(false);
        /* 提高网页渲染的优先级 */
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        /* 设置显示水平滚动条,就是网页右边的滚动条.我这里设置的不显示 */
        mWebView.setHorizontalScrollBarEnabled(false);
        /* 指定垂直滚动条是否有叠加样式 */
        mWebView.setVerticalScrollbarOverlay(true);
        /* 设置滚动条的样式 */
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                collapsingtoolbarlayout.setTitle(s);
            }
        });
    }

    @Override
    public void onBackPressed() {
        NestedScrollView parent = (NestedScrollView) mWebView.getParent();
        parent.removeAllViews();
        mWebView.destroy();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
