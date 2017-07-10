package com.dch.test.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

/**
 * 作者：MrCoder on 2017/7/10 14:30
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class WaterView extends View {


    private Paint mWaterPaint;
    private int defaultWaterColor = Color.RED;
    private int defaultCircleRadius = 40;
    private int mLittleCircleRadius = 40;
    private int mMoveY = 0;
    private int mState = STATE_DEFAULT;//水滴的状态
    private static final int STATE_DEFAULT = 0X001; //默认
    private static final int STATE_DRAG = 0X002; //拖拽
    private static final int STATE_GATHER = 0X003;//汇合
    private int mWaterCenterX = 0, mWaterCenterY = 0;
    private int mCircleCenterX = 0, mCircleCenterY = 0;
    private static final int DEFAULT_MIN_CHANGE_STATE_HEIGHT = 20;
    private static final int DEFAULT_MAX_CHANGE_STATE_HEIGHT = 200;
    private Path mBezierPath;
    private ValueAnimator mGatherAnimator;

    public WaterView(Context context) {
        this(context, null);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        mWaterCenterX = w / 2;
//        mWaterCenterY = h / 2;
//        mCircleCenterX = mWaterCenterX;
//        mCircleCenterY = mWaterCenterY;
    }

    private void init(Context context, AttributeSet attrs) {
        mWaterPaint = new Paint();
        mWaterPaint.setStyle(Paint.Style.FILL);
        mWaterPaint.setAntiAlias(true);
        mWaterPaint.setColor(defaultWaterColor);

        mBezierPath = new Path();
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWaterCenterX = getWidth() / 2;
                mWaterCenterY = getHeight() / 2;
                mCircleCenterX = mWaterCenterX;
                mCircleCenterY = mWaterCenterY;
                invalidate();
            }
        });

        mGatherAnimator = ValueAnimator.ofInt(mMoveY, 0);
        mGatherAnimator.setDuration(400);
        mGatherAnimator.setInterpolator(new DecelerateInterpolator());
        mGatherAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                setMoveState(animatedValue);
                if (0 == animatedValue) {
                    mState = STATE_DEFAULT;

                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //画小水滴
        canvas.drawCircle(mWaterCenterX, mWaterCenterY, mLittleCircleRadius, mWaterPaint);

        if (mState == STATE_DRAG) {
            //画移动水滴
            canvas.drawCircle(mCircleCenterX, mCircleCenterY, defaultCircleRadius, mWaterPaint);
            //画贝塞尔曲线
//            calculateBezier();
            mBezierPath.reset();
            mBezierPath.moveTo(mWaterCenterX - mLittleCircleRadius, mWaterCenterY);
            mBezierPath.quadTo(mWaterCenterX, mWaterCenterY - mMoveY / 2, mWaterCenterX - defaultCircleRadius, mCircleCenterY);
            mBezierPath.lineTo(mWaterCenterX + defaultCircleRadius, mCircleCenterY);
            mBezierPath.quadTo(mWaterCenterX, mWaterCenterY - mMoveY / 2, mWaterCenterX + mLittleCircleRadius, mWaterCenterY);
            mBezierPath.close();
            canvas.drawPath(mBezierPath, mWaterPaint);
        }
    }


    public void setMoveState(int moveY) {
        if (moveY < 0) {
            throw new RuntimeException("WaterView移动y值必须大于0");
        }
        this.mMoveY = moveY / 2;
        if (mMoveY > 120) {
            mMoveY = 120;
        }
        mState = STATE_DRAG;
        mLittleCircleRadius = (defaultCircleRadius) * ((DEFAULT_MAX_CHANGE_STATE_HEIGHT - mMoveY - DEFAULT_MIN_CHANGE_STATE_HEIGHT)) / DEFAULT_MAX_CHANGE_STATE_HEIGHT;
        if (moveY > DEFAULT_MIN_CHANGE_STATE_HEIGHT) {
            mCircleCenterY = mWaterCenterY - moveY + DEFAULT_MIN_CHANGE_STATE_HEIGHT;
            Log.d("aaa","setMoveState: "+mCircleCenterY);
        } else {
            mCircleCenterY = mWaterCenterY;
        }
        invalidate();
    }

    public void setGatherState() {
        //移动距离超出最大值, 可以下拉刷新了,即隐藏水滴
        if (mState != STATE_GATHER) {
            mState = STATE_GATHER;
            mGatherAnimator.start();
        }
    }

    public void resetState() {
        mState = STATE_DEFAULT;
        mMoveY = 0;
        mWaterCenterX = getWidth() / 2;
        mWaterCenterY = getHeight() / 2;
        mCircleCenterX = mWaterCenterX;
        mCircleCenterY = mWaterCenterY;
        invalidate();
    }

}
