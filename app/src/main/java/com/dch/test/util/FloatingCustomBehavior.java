package com.dch.test.util;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.logging.Logger;

import jameson.io.library.util.LogUtils;

/**
 * 作者：MrCoder on 2017/7/4 12:13
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class FloatingCustomBehavior extends FloatingActionButton.Behavior {

    private FastOutLinearInInterpolator fastOutLinearInInterpolator = new FastOutLinearInInterpolator();

    public FloatingCustomBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewGroup.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    //正在滑出来
    private boolean isAnimationOut = false;
    //正在滑进去
    private boolean isAnimationIn = false;

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed > 0 && !isAnimationOut && child.getVisibility() == View.VISIBLE) {
            //fab滑出去
            animateOut(child);
        } else if (dyConsumed < 0 &&!isAnimationIn && child.getVisibility() != View.VISIBLE) {
            //fab滑进来
            animateIn(child);
        }
        LogUtils.d(String.valueOf(child.getVisibility()));
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    /**
     * 滑进屏幕
     * @param child
     */
    private void animateIn(FloatingActionButton child) {
        child.setVisibility(View.VISIBLE);
        ViewCompat.animate(child).translationY(-child.getHeight()).setInterpolator(fastOutLinearInInterpolator).setListener(new ViewPropertyAnimatorListenerAdapter(){
            @Override
            public void onAnimationStart(View view) {
                isAnimationIn = true;
                super.onAnimationStart(view);
            }

            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(View.VISIBLE);
                isAnimationIn = false;
                super.onAnimationEnd(view);
            }

            @Override
            public void onAnimationCancel(View view) {
                isAnimationIn = false;
                super.onAnimationCancel(view);
            }
        }).start();
    }

    /**
     * 滑出屏幕
     * @param child
     */
    private void animateOut(FloatingActionButton child) {
        ViewCompat.animate(child).translationY(child.getHeight()).setInterpolator(fastOutLinearInInterpolator).setListener(new ViewPropertyAnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(View view) {
                isAnimationOut = true;
                super.onAnimationStart(view);
            }

            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(View.GONE);
                isAnimationOut = false;
                super.onAnimationEnd(view);
            }

            @Override
            public void onAnimationCancel(View view) {
                isAnimationOut = false;
                super.onAnimationCancel(view);
            }
        }).start();
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }
}
