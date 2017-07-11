package com.dch.test.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by dch on 2017/7/11.
 */

public class ChrysanthemumLoadingView extends View {
    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;
    private int mSegmentWidth = 20;
    private int mRadius = 40;
    private int[] colors = {0x9999999, 0x888888, 0x777777, 0x666666, 0x555555, 0x4444444};
    private int control = 1;

    public ChrysanthemumLoadingView(Context context) {
        this(context, null);
    }

    public ChrysanthemumLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChrysanthemumLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#999999"));
        mPaint.setStrokeWidth(10);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < 12; i++) {
            mPaint.setAlpha(((i + 1 + control) % 12 * 255) / 12);
            canvas.drawLine(0 + mCenterX, mCenterY - 40, 0 + mCenterX, mCenterY - 80, mPaint);
            canvas.rotate(30, mCenterX, mCenterY);
        }


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(12, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                control = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }
}
