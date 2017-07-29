package com.dch.test.widget.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：MrCoder on 2017/7/27 17:15
 * 描述：自定义贝塞尔效果指示器indicator
 * 邮箱：daichuanhao@caijinquan.com
 */
public class BezierIndicatorView2 extends View {
    private final String TAG = "BezierIndicatorView";
    private boolean changePath = false;
    private int mDefaultIndicatorColor = Color.WHITE;
    private int mMoveIndicatorColor = Color.RED;
    private int mIndicatorCount = 5;
    private float mPreX = 0;
    private float mNextX = 0;
    private float mNextYTop = 0;
    private float mNextYBottom = 0;
    private float offSet = 0;
    private float mIndicatorRadius = 15;
    private float mIndicatorInterval = 10;
    private int mWidth;
    private int mHeight;
    private int mCurrentIndicatorPosition = 0;
    private float[] mIndicators;
    private float mDeltDistanceBetweenIndicator = 0;
    private Indicator mCurrentIndicator;
    private List<Indicator> mIndicatorList = new ArrayList<>();
    private Path mBezierPath = new Path();
    private Path mPreIndicatorPath = new Path();
    private Path mNextIndicatorPath = new Path();
    private Paint mDefaultIndicatorPaint;
    private Paint mMoveIndicatorPaint;
    private Paint mCircleLinePaint;
    private Indicator mPreIndicator;

    public BezierIndicatorView2(Context context) {
        this(context, null);
    }

    public BezierIndicatorView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierIndicatorView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        mCircleLinePaint = new Paint();
        mCircleLinePaint.setAntiAlias(true);
        mCircleLinePaint.setColor(mDefaultIndicatorColor);
        mCircleLinePaint.setStyle(Paint.Style.STROKE);
        mCircleLinePaint.setStrokeWidth(mIndicatorRadius*2);
    }

    public void setIndicatorList(int count) {
        this.mIndicatorCount = count;
        mIndicators = new float[10];
        for (int i = 0; i < mIndicatorCount; i++) {
            Indicator indicator = new Indicator(mIndicatorRadius * (i * 2 + 1) + (i + 1) * mIndicatorInterval, mHeight / 2);
            mIndicatorList.add(indicator);
            mIndicators[2*i] = indicator.x;
            mIndicators[2*i+1] = indicator.y;
            if (i == mCurrentIndicatorPosition) {
                mCurrentIndicator = new Indicator(indicator.x, indicator.y);
            }
        }
        generatePrePath();

        generateAfterPath();

        invalidate();
    }

    private void generateAfterPath() {
        mBezierPath.reset();
        mBezierPath.moveTo(mCurrentIndicator.x,mCurrentIndicator.y+mIndicatorRadius);
        mNextYTop = mHeight/2-mIndicatorRadius*offSet;
        mNextYBottom = mHeight/2+mIndicatorRadius*offSet;
        mBezierPath.quadTo(mCurrentIndicator.x+mIndicatorRadius+mIndicatorInterval/2,mNextYBottom,mCurrentIndicator.x+mIndicatorInterval+mIndicatorRadius*2,mHeight/2+mIndicatorRadius);
        mBezierPath.lineTo(mCurrentIndicator.x+mIndicatorInterval+mIndicatorRadius*2,mHeight/2-mIndicatorRadius);
        mBezierPath.quadTo(mCurrentIndicator.x+mIndicatorRadius+mIndicatorInterval/2,mNextYTop,mCurrentIndicator.x,mCurrentIndicator.y-mIndicatorRadius);
        mBezierPath.close();
    }

    private void generatePrePath() {
        mPreIndicatorPath.reset();
        mPreIndicatorPath.moveTo(mCurrentIndicator.x,mCurrentIndicator.y+mIndicatorRadius);
        mPreX  = mCurrentIndicator.x+mIndicatorRadius*2+mIndicatorInterval*this.offSet;
        mPreIndicatorPath.quadTo(mPreX,mHeight/2,mCurrentIndicator.x,mHeight/2-mIndicatorRadius);
        mPreIndicatorPath.close();

        mNextIndicatorPath.reset();
        mNextIndicatorPath.moveTo(mCurrentIndicator.x+mIndicatorInterval+mIndicatorRadius*2,mHeight/2+mIndicatorRadius);
        mNextX = mCurrentIndicator.x+mIndicatorRadius+mIndicatorInterval/2+mIndicatorInterval*(0.5f-offSet);
        mNextIndicatorPath.quadTo(mNextX,mHeight/2,mCurrentIndicator.x+mIndicatorInterval+mIndicatorRadius*2,mHeight/2-mIndicatorRadius);
        mNextIndicatorPath.close();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = measuredWidth;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = measuredWidth + getPaddingLeft() + getPaddingRight();
        } else {
            mWidth = (int) (getPaddingLeft() + getPaddingRight() + (mIndicatorCount + 1) * mIndicatorInterval + mIndicatorCount * 2 * mIndicatorRadius);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = measuredHeight;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mHeight = measuredHeight + getPaddingTop() + getPaddingBottom();
        } else {
            mHeight = getPaddingTop() + getPaddingBottom() + 2 * (int) mIndicatorRadius + 10;
        }

        setMeasuredDimension(mWidth, mHeight);
        mIndicatorInterval = (mWidth - 2 * mIndicatorCount * mIndicatorRadius) / (1 + mIndicatorCount);
        mDeltDistanceBetweenIndicator = mIndicatorRadius * 2 + mIndicatorInterval;
    }

    Indicator indicator;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mIndicatorList.size(); i++) {
                indicator = mIndicatorList.get(i);
                canvas.drawCircle(indicator.x, indicator.y, mIndicatorRadius, mDefaultIndicatorPaint);
        }

        canvas.drawCircle(mCurrentIndicator.x, mCurrentIndicator.y, mIndicatorRadius, mMoveIndicatorPaint);
        if (changePath){
            canvas.drawPath(mBezierPath, mMoveIndicatorPaint);
        } else {
            canvas.drawPath(mPreIndicatorPath,mDefaultIndicatorPaint);
            canvas.drawPath(mNextIndicatorPath,mDefaultIndicatorPaint);
        }

    }

    private int mLastPosition = -1;

    /**
     * @param xOffset xOffSet是一个介于0~1之间的映射两个indicator距离的值
     */
    public void indicatorMove(float xOffset) {
        if (mIndicatorList.size() > 0) {
            if (xOffset < 0 && xOffset > 1) {
                throw new RuntimeException("offset偏移量必须介于0~1之间");
            }
            this.offSet = xOffset;
            if (xOffset < 0.1) {
                mLastPosition = mCurrentIndicatorPosition;
            }
            if (xOffset >0.5){
                changePath = true;
                generateAfterPath();
            } else {
                changePath = false;
                generatePrePath();
            }


            invalidate();
        }
    }


    public void setCurrentPosition(final int position) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mBezierPath.reset();
                mCurrentIndicatorPosition = position;
                mCurrentIndicator.x = mIndicatorList.get(position).x;
                invalidate();
            }
        },10);


    }

    private class Indicator {
        public float x = -1;
        public float y = -1;

        public Indicator(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
