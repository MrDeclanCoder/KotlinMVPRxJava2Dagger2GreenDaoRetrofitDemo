package com.dch.test.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.dch.test.R;
import com.dch.test.widget.SwipeLayout;
import com.dch.test.widget.WaterRefreshView;
import com.dch.test.widget.banner.BezierIndicatorView;

public class TestViewActivity extends AppCompatActivity {

    private int transY;
    private AnimatedVectorDrawable drawable;
    private AnimatedVectorDrawable drawableTick;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        final WaterRefreshView waterRefreshView = (WaterRefreshView) findViewById(R.id.waterrefreshview);
        waterRefreshView.setOnRefreshListener(new WaterRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawableTick.start();
                        waterRefreshView.refreshSuccess();
                    }
                }, 200);
            }
        });
        final SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipe_edit_layout);
        swipeLayout.setOnMenuClickListener(new SwipeLayout.OnMenuClickListener() {
            @Override
            public void onMenuClick(View v) {
                transWaterRefreshView(waterRefreshView);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(swipeLayout, "alpha", 1f, 0f);
                alpha.setDuration(500);
                alpha.setInterpolator(new DecelerateInterpolator());
                alpha.start();
                alpha.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
//                        swipeLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
//        final SwipeLayout swipeLayout2 = (SwipeLayout) findViewById(R.id.swipe_edit_layout2);
//        swipeLayout2.setOnMenuClickListener(new SwipeLayout.OnMenuClickListener() {
//            @Override
//            public void onMenuClick(View v) {
////                transWaterRefreshView(waterRefreshView);
//                ObjectAnimator alpha = ObjectAnimator.ofFloat(swipeLayout2, "translationX", 0,swipeLayout2.getWidth());
//                alpha.setDuration(500);
//                alpha.setInterpolator(new DecelerateInterpolator());
//                alpha.start();
//                alpha.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        swipeLayout2.setVisibility(View.GONE);
//                    }
//                });
//            }
//        });
        swipeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                transY = swipeLayout.getHeight();
                swipeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ImageView iv_vector = (ImageView) findViewById(R.id.iv_test_view_vector_jrtt);
        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) iv_vector.getDrawable();
        drawable.start();
        ImageView iv_tick = (ImageView) findViewById(R.id.iv_test_view_vector_tick);
        drawableTick = (AnimatedVectorDrawable) iv_tick.getDrawable();
        drawableTick.start();

        final BezierIndicatorView bezierIndicatorView = (BezierIndicatorView) findViewById(R.id.bezierIndicatorView);
        bezierIndicatorView.setIndicatorList(5);
        ViewPager viewPager = (ViewPager) findViewById(R.id.test_viewpager);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("scroll",String.valueOf(positionOffset));
                bezierIndicatorView.indicatorMove(positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
//                bezierIndicatorView.setCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = new TextView(container.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setText("我是第" + (position + 1) + "个位置");
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView(container.getChildAt(position));
        }
    }

    private void transWaterRefreshView(WaterRefreshView waterRefreshView) {
        final ObjectAnimator translateY = ObjectAnimator.ofFloat(waterRefreshView, "translationY", waterRefreshView.getTop(), -transY);
        translateY.setDuration(400);
        translateY.setInterpolator(new DecelerateInterpolator());
        translateY.setStartDelay(400);
        translateY.start();
    }
}
