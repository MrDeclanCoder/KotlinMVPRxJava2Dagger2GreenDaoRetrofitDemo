package com.dch.test.ui;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Fade;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.util.ActivityUtils;
import com.dch.test.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

/**
 * 作者：Dch on 2017/4/17 10:51
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class GuideActivity extends BaseActivity {

    private List<ImageView> imageViews = new ArrayList<>();
    private ObjectAnimator alpha;

    @BindView(R.id.indicator_view)
    SpringIndicator indicator_view;

    @BindView(R.id.viewpager_guide)
    ScrollerViewPager viewPager;

    @BindView(R.id.bt_gohome)
    Button button;

    @OnClick(R.id.bt_gohome)
    public void goHomeButtonClick(View view){
        ActivityUtils.startActivity(this,HomeActivity.class);
        finish();
    }

    @Override
    protected void initData() {
    }

    @TargetApi(21)
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(100);
        getWindow().setEnterTransition(fade);
    }

    @Override
    protected void initView() {
        StatusBarUtils.setImage(this);
        setupWindowAnimations();
        alpha = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f).setDuration(1000);
        int[] guideImgs = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4};
        for (int i = 0; i < guideImgs.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setBackgroundResource(guideImgs[i]);
            imageViews.add(imageView);
        }
//        PagerModelManager pagerModelManager = new PagerModelManager();
//        pagerModelManager.
        viewPager.setAdapter(new GuideAdapter());
        viewPager.fixScrollSpeed();
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        indicator_view.setViewPager(viewPager);
        indicator_view.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    if (alpha.isRunning()){
                        alpha.cancel();
                    }
                    alpha.start();
                    button.setVisibility(View.VISIBLE);
                    indicator_view.setVisibility(View.GONE);
                } else {
                    button.setVisibility(View.GONE);
                    indicator_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_guide;
    }

    private class GuideAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
