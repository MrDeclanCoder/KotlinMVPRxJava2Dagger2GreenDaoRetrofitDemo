package com.dch.test.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.UiModeManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.base.BaseApplication;
import com.dch.test.entity.MyFavorite;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import greendao.gen.MyFavoriteDao;

/**
 * 作者：Dch on 2017/4/17 17:02
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private ObjectAnimator alphaAnimation;
    private String id;

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
    @BindView(R.id.floatingactionbutton_details)
    FloatingActionButton floatingactionbutton_details;
    private MyFavoriteDao myFavoriteDao;
    private String imgurl;
    private String url;
    private String title;
    private String content;
    private MyFavorite myFavorite;
    private ObjectAnimator scaleXAnim;
    private ObjectAnimator scaleYAnim;
    private AnimatorSet animatorSet;

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
        floatingactionbutton_details.setOnClickListener(this);
        initAnimation();
    }

    private void initAnimation() {
        scaleXAnim = ObjectAnimator.ofFloat(floatingactionbutton_details,"scaleX",1.3f,0.8f,1.0f);
        scaleYAnim = ObjectAnimator.ofFloat(floatingactionbutton_details,"scaleY",1.3f,0.8f,1.0f);
        animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnim).with(scaleYAnim);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new DecelerateInterpolator());
    }


    @Override
    protected void initData() {
        imgurl = getIntent().getStringExtra("imgurl");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        Glide.with(this).load(imgurl).placeholder(R.drawable.guide4).into(iv_gank_detail);
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");
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

        myFavoriteDao = BaseApplication.application.getDaoSession().getMyFavoriteDao();
        myFavorite = new MyFavorite();
        myFavorite.setId(null);
        myFavorite.setFavoriteId(id);
        myFavorite.setTitle(title);
        myFavorite.setContentDiscription(content);
        myFavorite.setUrl(url);
        myFavorite.setImgUrl(imgurl);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        myFavorite.setCollectTime(sdf.format(new Date()));

        if (judgeExitAndInsert()) {
           //已存在
            floatingactionbutton_details.setImageResource(android.R.drawable.btn_star_big_on);
            floatingactionbutton_details.postDelayed(animationRunnable,200);
        }
    }
    private Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            animatorSet.start();
        }
    };

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
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                if (s.length() > 15) {
                    s = s.substring(0, 15) + "...";
                }
                collapsingtoolbarlayout.setTitle(s);
            }
        });
    }

    @Override
    public void onBackPressed() {
        NestedScrollView parent = (NestedScrollView) mWebView.getParent();
        if (null != parent) {
            parent.removeAllViews();
        }
        mWebView.destroy();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingactionbutton_details:
                //收藏逻辑
                if (judgeExitAndInsert()) {
                    Snackbar.make(floatingactionbutton_details, "已收藏", Snackbar.LENGTH_SHORT).show();
                } else {
                    //加入收藏
                    insertToDB(myFavorite);
                }
                break;
        }
    }

    /**
     * 判断是否存在
     * @return true:存在  false:不存在
     */
    private boolean judgeExitAndInsert() {
        boolean isExit = false;
        final List<MyFavorite> myFavorites = myFavoriteDao.queryBuilder().where(MyFavoriteDao.Properties.FavoriteId.eq(id)).list();
        if (null != myFavorites && myFavorites.size() > 0) {
            //判断是否存在
            for (MyFavorite favorite : myFavorites) {
                if (favorite.getFavoriteId().equals(id)) {
                    isExit = true;
                }
            }
        }
        return isExit;
    }

    private void insertToDB(final MyFavorite myFavorite) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this, R.style.CustomAlertDialog);
        builder.setTitle("收藏");
        builder.setMessage("确定加入收藏 ?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确定加入收藏
                try {
                    myFavoriteDao.insert(myFavorite);
                    Snackbar.make(floatingactionbutton_details, "收藏成功", Snackbar.LENGTH_SHORT).show();
                    floatingactionbutton_details.setImageResource(android.R.drawable.btn_star_big_on);
                    floatingactionbutton_details.postDelayed(animationRunnable,200);
                } catch (Exception e) {
                    Snackbar.make(floatingactionbutton_details, "收藏失败", Snackbar.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
