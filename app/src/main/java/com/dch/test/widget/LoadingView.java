package com.dch.test.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 作者：MrCoder on 2017/11/1 0001 16:12
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class LoadingView extends View {

    private Paint mBallPaint;
    private boolean isLeft = true;
    private ValueAnimator mLeftAnim;
    private ValueAnimator mRightAnim;
    private int mLeftX;
    private int mRightX;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(700, 200);
    }

    private void init() {
        mBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBallPaint.setColor(Color.BLUE);
        mBallPaint.setStrokeWidth(20);

        mLeftAnim = ValueAnimator.ofInt(160, 50, 160);
        mLeftAnim.setDuration(600);
        mLeftAnim.setRepeatCount(0);
        mLeftAnim.setInterpolator(new LinearInterpolator());
        mLeftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLeftX = ((int) animation.getAnimatedValue());
                invalidate();
            }
        });
        mLeftAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isLeft = false;
                mRightAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mRightAnim = ValueAnimator.ofInt(360, 470, 360);
        mRightAnim.setDuration(600);
        mRightAnim.setRepeatCount(0);
        mRightAnim.setInterpolator(new LinearInterpolator());
        mRightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRightX = ((int) animation.getAnimatedValue());
                invalidate();
            }
        });
        mRightAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isLeft = true;
                mLeftAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        isLeft = true;
        mLeftAnim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 4; i++) {
            canvas.drawCircle(200 + i * 40, 100, 20, mBallPaint);
        }

        if (isLeft) {
            canvas.drawCircle(mLeftX, 100, 20, mBallPaint);
            canvas.drawCircle(360, 100, 20, mBallPaint);
        } else {
            canvas.drawCircle(mRightX, 100, 20, mBallPaint);
            canvas.drawCircle(160, 100, 20, mBallPaint);
        }

    }
}
