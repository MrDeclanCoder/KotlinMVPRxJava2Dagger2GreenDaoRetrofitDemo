package com.dch.test.widget.banner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：MrCoder on 2017/7/27 17:15
 * 描述：自定义贝塞尔效果指示器indicator
 * 邮箱：daichuanhao@caijinquan.com
 */
public class BezierIndicatorView extends View implements ViewPager.OnPageChangeListener {
    private final String TAG = "BezierIndicatorView";
    private final float RATIO = 0.551915024494f;
    private boolean mUnderHalfDrawing = true;
    private boolean mMoveRight = true;
    private int mDefaultIndicatorColor = Color.BLUE;
    private int mMoveIndicatorColor = Color.RED;
    private int mIndicatorCount = 5;
    private int mHeight;
    private int mCurrentIndicatorPosition = 0;
    private float mOffSet = 0;
    private float mIndicatorRadius = 10;
    private float deltLittle = mIndicatorRadius * RATIO;
    private float delt = deltLittle;
    private float mIndicatorInterval = 10;
    private List<Indicator> mDefaultIndicatorList = new ArrayList<>();
    private Path mCurrentPath = new Path();
    private Path mPath = new Path();
    private RectF mRectF = new RectF();
    private List<Path> mIndicatorPathList = new ArrayList<>();
    private List<Path> mCirclePaths = new ArrayList<>();
    private Paint mDefaultIndicatorPaint;
    private Paint mMoveIndicatorPaint;

    private PagerAdapter mPagerAdapter;

    public BezierIndicatorView(Context context) {
        this(context, null);
    }

    public BezierIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultIndicatorPaint = new Paint();
        mDefaultIndicatorPaint.setAntiAlias(true);
        mDefaultIndicatorPaint.setColor(mDefaultIndicatorColor);
        mDefaultIndicatorPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mMoveIndicatorPaint = new Paint();
        mMoveIndicatorPaint.setAntiAlias(true);
        mMoveIndicatorPaint.setColor(mMoveIndicatorColor);
        mMoveIndicatorPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        if (mIndicatorPathList.size() > 0) mIndicatorPathList.clear();
        if (mCirclePaths.size() > 0) mCirclePaths.clear();
        if (null != pagerAdapter) {
            this.mPagerAdapter = pagerAdapter;
        }
        if (mHeight != 0) {
            this.mIndicatorCount = mPagerAdapter.getCount();
            for (int i = 0; i < mIndicatorCount; i++) {
                Indicator indicator = generateIndicator(i);
                Path path = calculateUnderHalfPath(indicator);
                mCirclePaths.add(path);
                mDefaultIndicatorList.add(indicator);
            }
            mCurrentPath = mCirclePaths.get(0);
            requestLayout();
            invalidate();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int mWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = measuredWidth;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = measuredWidth + getPaddingLeft() + getPaddingRight();
        } else {
            mWidth = (int) (getPaddingLeft() + getPaddingRight() + (mIndicatorCount + 1) * mIndicatorInterval + mIndicatorCount * 2 * mIndicatorRadius);
        }

        mHeight = getSuggestedMeasuredHeight();
        setMeasuredDimension(mWidth, mHeight);
        mIndicatorInterval = (mWidth - 2 * mIndicatorCount * mIndicatorRadius) / (1 + mIndicatorCount);
        if (mPagerAdapter != null && mDefaultIndicatorList.size() == 0) {
            setAdapter(mPagerAdapter);
        }
    }

    private int getSuggestedMeasuredHeight() {
        return 2 * (getPaddingTop() + getPaddingBottom() + 2 * (int) mIndicatorRadius + 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画默认indicator
        for (int i = 0; i < mCirclePaths.size(); i++) {
            canvas.drawPath(mCirclePaths.get(i), mDefaultIndicatorPaint);
        }
        //画正在移动indicator
        canvas.drawPath(mPath, mDefaultIndicatorPaint);
        //绘制当前指向indicator
        canvas.drawPath(mCurrentPath, mMoveIndicatorPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        if (positionOffset != 0) {
//
//            if (lastValue >= positionOffsetPixels) {
//                //右滑
//                isLeft = false;
////                mOffSet = 1 - positionOffset;
////                mUnderHalfDrawing = mOffSet < 0.5;
////                invalidatePath2();
//            } else if (lastValue < positionOffsetPixels) {
//                //左滑
//                isLeft = true;
//                mOffSet = positionOffset;
//                mUnderHalfDrawing = mOffSet < 0.5;
//                invalidatePath2();
//            }
//        }
//        lastValue = positionOffsetPixels;

        mCurrentIndicatorPosition = position;
        mOffSet = positionOffset;
        mUnderHalfDrawing = mOffSet < 0.5;
        invalidatePath2();
    }

    private void invalidatePath2() {
        if (mUnderHalfDrawing) {
            generateUnderHalfPath();

        } else {
            generateAboveHalfOffsetPath();
        }
        invalidate();
    }

    @Override
    public void onPageSelected(final int position) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mMoveRight = mCurrentIndicatorPosition < position;
                mCurrentIndicatorPosition = position;
                mOffSet = 0;
                mPath.reset();
                indicatorTranslate();
                invalidate();
            }
        },500);

    }


    private void indicatorTranslate() {
        ValueAnimator valueAnimator;
        if (mMoveRight) {
            valueAnimator = ValueAnimator.ofFloat(0, mIndicatorRadius * 2 + mIndicatorInterval);
        } else {
            valueAnimator = ValueAnimator.ofFloat(0, -(mIndicatorRadius * 2 + mIndicatorInterval));
        }

        valueAnimator.setDuration(400);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mCurrentPath = calculateCurrentPath(mDefaultIndicatorList.get(mCurrentIndicatorPosition), animatedValue);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurrentPath = mCirclePaths.get(mCurrentIndicatorPosition);
                invalidate();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        valueAnimator.setStartDelay(100);
        valueAnimator.start();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private Indicator generateIndicator(int i) {
        Indicator indicator = new Indicator();
        indicator.point0.x = mIndicatorRadius * (i * 2 + 1) + (i + 1) * mIndicatorInterval;
        indicator.point0.y = mHeight / 2 + mIndicatorRadius;

        indicator.point1.x = mIndicatorRadius * (i * 2 + 1) + (i + 1) * mIndicatorInterval + delt;
        indicator.point1.y = mHeight / 2 + mIndicatorRadius;

        indicator.point2.x = mIndicatorRadius * (i * 2 + 2) + (i + 1) * mIndicatorInterval;
        indicator.point2.y = mHeight / 2 + delt;

        indicator.point3.x = mIndicatorRadius * (i * 2 + 2) + (i + 1) * mIndicatorInterval;
        indicator.point3.y = mHeight / 2;

        indicator.point4.x = mIndicatorRadius * (i * 2 + 2) + (i + 1) * mIndicatorInterval;
        indicator.point4.y = mHeight / 2 - delt;


        indicator.point5.x = mIndicatorRadius * (i * 2 + 1) + (i + 1) * mIndicatorInterval + delt;
        indicator.point5.y = mHeight / 2 - mIndicatorRadius;

        indicator.point6.x = mIndicatorRadius * (i * 2 + 1) + (i + 1) * mIndicatorInterval;
        indicator.point6.y = mHeight / 2 - mIndicatorRadius;

        indicator.point7.x = mIndicatorRadius * (i * 2 + 1) + (i + 1) * mIndicatorInterval - delt;
        indicator.point7.y = mHeight / 2 - mIndicatorRadius;

        indicator.point8.x = mIndicatorRadius * (i * 2) + (i + 1) * mIndicatorInterval;
        indicator.point8.y = mHeight / 2 - delt;

        indicator.point9.x = mIndicatorRadius * (i * 2) + (i + 1) * mIndicatorInterval;
        indicator.point9.y = mHeight / 2;

        indicator.point10.x = mIndicatorRadius * (i * 2) + (i + 1) * mIndicatorInterval;
        indicator.point10.y = mHeight / 2 + delt;

        indicator.point11.x = mIndicatorRadius * (i * 2 + 1) + (i + 1) * mIndicatorInterval - delt;
        indicator.point11.y = mHeight / 2 + mIndicatorRadius;

        return indicator;
    }

    private Path calculateCurrentPath(Indicator indicator, float deltX) {
        Path mBezierPath = new Path();
        mBezierPath.moveTo(indicator.point0.x + deltX, indicator.point0.y);
        mBezierPath.cubicTo(indicator.point1.x + deltX, indicator.point1.y, indicator.point2.x + deltX, indicator.point2.y, indicator.point3.x + deltX, indicator.point3.y);
        mBezierPath.cubicTo(indicator.point4.x + deltX, indicator.point4.y, indicator.point5.x + deltX, indicator.point5.y, indicator.point6.x + deltX, indicator.point6.y);
        mBezierPath.cubicTo(indicator.point7.x + deltX, indicator.point7.y, indicator.point8.x + deltX, indicator.point8.y, indicator.point9.x + deltX, indicator.point9.y);
        mBezierPath.cubicTo(indicator.point10.x + deltX, indicator.point10.y, indicator.point11.x + deltX, indicator.point11.y, indicator.point0.x + deltX, indicator.point0.y);
        mBezierPath.close();
        return mBezierPath;
    }

    private Path calculateUnderHalfPath(Indicator indicator) {
        Path mBezierPath = new Path();
        mBezierPath.moveTo(indicator.point0.x, indicator.point0.y);
        mBezierPath.cubicTo(indicator.point1.x, indicator.point1.y, indicator.point2.x, indicator.point2.y, indicator.point3.x, indicator.point3.y);
        mBezierPath.cubicTo(indicator.point4.x, indicator.point4.y, indicator.point5.x, indicator.point5.y, indicator.point6.x, indicator.point6.y);
        mBezierPath.cubicTo(indicator.point7.x, indicator.point7.y, indicator.point8.x, indicator.point8.y, indicator.point9.x, indicator.point9.y);
        mBezierPath.cubicTo(indicator.point10.x, indicator.point10.y, indicator.point11.x, indicator.point11.y, indicator.point0.x, indicator.point0.y);
        mBezierPath.close();
        return mBezierPath;
    }

    private void generateAboveHalfOffsetPath() {
        Indicator preIndicator = mDefaultIndicatorList.get(mCurrentIndicatorPosition);
        Indicator nextIndicator = mDefaultIndicatorList.get(mCurrentIndicatorPosition + 1);
        float middleX = (preIndicator.point2.x + mIndicatorInterval / 2);
        float topY;
        float bottomY;
        float deltY = mOffSet * mIndicatorRadius * (mOffSet - 0.5f) * 2;
        topY = mHeight / 2 - deltY;
        bottomY = mHeight / 2 + deltY;

        mPath.reset();
        mPath.moveTo(preIndicator.point0.x, preIndicator.point0.y);
        mPath.quadTo(middleX, bottomY, nextIndicator.point0.x, nextIndicator.point0.y);
        mPath.cubicTo(nextIndicator.point1.x, nextIndicator.point1.y, nextIndicator.point2.x, nextIndicator.point2.y, nextIndicator.point3.x, nextIndicator.point3.y);
        mPath.cubicTo(nextIndicator.point4.x, nextIndicator.point4.y, nextIndicator.point5.x, nextIndicator.point5.y, nextIndicator.point6.x, nextIndicator.point6.y);
        mPath.quadTo(middleX, topY, preIndicator.point6.x, preIndicator.point6.y);
        mPath.cubicTo(preIndicator.point7.x, preIndicator.point7.y, preIndicator.point8.x, preIndicator.point8.y, preIndicator.point9.x, preIndicator.point9.y);
        mPath.cubicTo(preIndicator.point10.x, preIndicator.point10.y, preIndicator.point11.x, preIndicator.point11.y, preIndicator.point0.x, preIndicator.point0.y);
        mPath.close();
    }

    private void generateUnderHalfPath() {
        Indicator indicator = mDefaultIndicatorList.get(mCurrentIndicatorPosition);
        float deltX = mIndicatorInterval * mOffSet;
        mPath.reset();
        mPath.moveTo(indicator.point0.x, indicator.point0.y);
        mPath.cubicTo(indicator.point1.x, indicator.point1.y, indicator.point2.x + deltX, indicator.point2.y, indicator.point3.x + deltX, indicator.point3.y);
        mPath.cubicTo(indicator.point4.x + deltX, indicator.point4.y, indicator.point5.x, indicator.point5.y, indicator.point6.x, indicator.point6.y);
        mPath.cubicTo(indicator.point7.x, indicator.point7.y, indicator.point8.x, indicator.point8.y, indicator.point9.x, indicator.point9.y);
        mPath.cubicTo(indicator.point10.x, indicator.point10.y, indicator.point11.x, indicator.point11.y, indicator.point0.x, indicator.point0.y);
        mPath.close();
        if (mCurrentIndicatorPosition + 1 < mDefaultIndicatorList.size()) {
            Indicator nextIndicator = mDefaultIndicatorList.get(mCurrentIndicatorPosition + 1);
            mPath.moveTo(nextIndicator.point0.x, nextIndicator.point0.y);
            mPath.cubicTo(nextIndicator.point1.x, nextIndicator.point1.y, nextIndicator.point2.x, nextIndicator.point2.y, nextIndicator.point3.x, nextIndicator.point3.y);
            mPath.cubicTo(nextIndicator.point4.x, nextIndicator.point4.y, nextIndicator.point5.x, nextIndicator.point5.y, nextIndicator.point6.x, nextIndicator.point6.y);
            mPath.cubicTo(nextIndicator.point7.x, nextIndicator.point7.y, nextIndicator.point8.x - deltX, nextIndicator.point8.y, nextIndicator.point9.x - deltX, nextIndicator.point9.y);
            mPath.cubicTo(nextIndicator.point10.x - deltX, nextIndicator.point10.y, nextIndicator.point11.x, nextIndicator.point11.y, nextIndicator.point0.x, nextIndicator.point0.y);
            mPath.close();
        }
    }

    private class Point {
        public float x = -1;
        public float y = -1;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private class Indicator {
        public Point point0 = new Point();
        public Point point1 = new Point();
        public Point point2 = new Point();
        public Point point3 = new Point();
        public Point point4 = new Point();
        public Point point5 = new Point();
        public Point point6 = new Point();
        public Point point7 = new Point();
        public Point point8 = new Point();
        public Point point9 = new Point();
        public Point point10 = new Point();
        public Point point11 = new Point();

    }
}
