package com.dch.test.util;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 作者：Dch on 2017/4/17 19:30
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class TransitionUtils {
    /**
     *创建Reveal动画并执行
     *
     * @param viewRoot 要执行动画的布局
     * @param color 揭露的颜色
     * @param cx 揭露点的X坐标
     * @param cy 揭露点的Y坐标
     * @return 返回创建成功的动画
     */
    @TargetApi(21)
    private Animator animateRevealColorFromCoordinates(ViewGroup viewRoot, int color, int cx, int cy, Activity activity) {
        float finalRadius= (float) Math.hypot(viewRoot.getWidth(),viewRoot.getHeight());

        Animator anim= ViewAnimationUtils.createCircularReveal(viewRoot,cx,cy,0,finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(activity,color));
        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
        return anim;
    }

}
