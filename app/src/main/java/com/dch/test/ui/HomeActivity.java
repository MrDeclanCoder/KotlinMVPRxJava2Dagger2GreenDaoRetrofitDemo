package com.dch.test.ui;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.base.BaseApplication;
import com.dch.test.base.BaseFragment;
import com.dch.test.contract.presenter.HomePresenter;
import com.dch.test.di.activity.HomePresenterModule;
import com.dch.test.ui.fragment.CsdnBlogFragment;
import com.dch.test.ui.fragment.GankAndroidFragment;
import com.dch.test.ui.fragment.GankMeiziFragment;
import com.dch.test.util.ActivityUtils;
import com.dch.test.util.RxBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

//import com.dch.test.di.app.AppModule;
//import com.dch.test.di.app.DaggerAppComponent;

/**
 * 作者：MrCoder on 2017年4月10日 16:17:38
 * 描述：主界面
 * 邮箱：codermr@163.com
 */
public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<BaseFragment> mFragmentList = new ArrayList<>();
    private String[] titles = {"Android", "妹纸", "博客"};
    private int currentIndex = 0;
    private boolean SHOWING = false;

    @Inject
    HomePresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.tabs_home)
    TabLayout tabs_home;
    @BindView(R.id.vp_home)
    ViewPager mViewPager;

    @BindView(R.id.framelayout_test_home)
    FrameLayout framelayout_test_home;

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FabCloseEvent event){
        if (1 == event.status){
            //close
            if (SHOWING){
                SHOWING = !SHOWING;
                hideFabLayout();
            }
        }

    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClick();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        mFragmentList.add(GankAndroidFragment.newInstance());
        mFragmentList.add(GankMeiziFragment.newInstance());
        mFragmentList.add(CsdnBlogFragment.newInstance());

        RxBus.getInstance().registSubject(new RxBus.CallBack<String>() {
            @Override
            public void onNext(String s) {
                toolbar.setTitle(s);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        mViewPager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager()));
        tabs_home.setupWithViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                RxBus.getInstance().post(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(0);
        RxBus.getInstance().post(titles[0]);
    }

    private void fabClick() {
    /* Snackbar.make(view, "我的收藏", Snackbar.LENGTH_LONG)
             .setAction("前往", new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     ActivityUtils.startActivity(HomeActivity.this, KotlinScrollingActivity.class);
                 }
             }).show();*/

        if (!SHOWING) {
            //显示
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //揭示动画
                Animator animatorShow = ViewAnimationUtils.createCircularReveal(framelayout_test_home,
                        framelayout_test_home.getWidth(),
                        framelayout_test_home.getHeight(),
                        0,
                        (float) Math.hypot(framelayout_test_home.getWidth(), framelayout_test_home.getHeight()));
                framelayout_test_home.setVisibility(View.VISIBLE);
                if (framelayout_test_home.getVisibility() == View.VISIBLE) {
                    animatorShow.setDuration(300);
                    animatorShow.start();
                }
            } else {
                framelayout_test_home.setVisibility(View.VISIBLE);
            }
        } else {
            //隐藏
            hideFabLayout();
        }

        SHOWING = !SHOWING;
    }

    private void hideFabLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animatorHide = ViewAnimationUtils.createCircularReveal(framelayout_test_home, framelayout_test_home.getWidth(), framelayout_test_home.getHeight(),
                    (float) Math.hypot(framelayout_test_home.getWidth(), framelayout_test_home.getHeight()), 0);
            animatorHide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    framelayout_test_home.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorHide.setDuration(300);
            animatorHide.start();
        } else {
            framelayout_test_home.setVisibility(View.GONE);
        }
    }


    protected HomePresenterModule getActivityModule() {
        return new HomePresenterModule(mFragmentList.get(currentIndex));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, CustomLeftDrawerActivity.class));
        } else if (id == R.id.nav_floating) {
            startActivity(new Intent(this, KotlinSettingsActivity.class));
        } else if (id == R.id.nav_music) {
            startActivity(new Intent(this, CustomLeftDrawerActivity.class));
        } else if (id == R.id.nav_tool) {
            startActivity(new Intent(this, KotlinSettingsActivity.class));
        } else if (id == R.id.nav_path) {
            startActivity(new Intent(this, LeadActivity.class));
        } else if (id == R.id.nav_favorite) {
            startActivity(new Intent(this, KotlinScrollingActivity.class));
        } else if (id == R.id.nav_wuziqi) {
            startActivity(new Intent(this, GameActivity.class));
        }
        return true;
    }


    private class HomeViewPagerAdapter extends FragmentPagerAdapter {

        public HomeViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
