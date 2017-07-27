package com.dch.test.widget.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：MrCoder on 2017/7/27 17:15
 * 描述：自定义贝塞尔效果指示器indicator
 * 邮箱：daichuanhao@caijinquan.com
 */
public class BezierIndicatorView extends View {
    private int mDefaultIndicatorColor = Color.BLUE;
    private int mMoveIndicatorColor = Color.RED;
    private int mIndicatorCount = 5;
    private float mIndicatorRadius = 25;
    private float mIndicatorInterval = 10;
    private int mWidth;
    private int mHeight;
    private int mCurrentIndicatorPosition = 0;
    private int mDeltDistanceBetweenIndicator = 0;
    private Indicator mCurrentIndicator;
    private List<Indicator> mIndicatorList = new ArrayList<>();
    private Path mBezierPath;
    private Paint mDefaultIndicatorPaint;
    private Paint mMoveIndicatorPaint;
    private Paint mBezierAreaPaint;

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
        mDefaultIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultIndicatorPaint.setAntiAlias(true);
        mDefaultIndicatorPaint.setColor(mDefaultIndicatorColor);
        mDefaultIndicatorPaint.setStyle(Paint.Style.FILL);
        mDefaultIndicatorPaint.setStrokeWidth(10);

        mMoveIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMoveIndicatorPaint.setAntiAlias(true);
        mMoveIndicatorPaint.setColor(mMoveIndicatorColor);
        mMoveIndicatorPaint.setStyle(Paint.Style.FILL);
        mMoveIndicatorPaint.setStrokeWidth(10);

        mBezierAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBezierAreaPaint.setAntiAlias(true);
        mBezierAreaPaint.setColor(mMoveIndicatorColor);
        mBezierAreaPaint.setStyle(Paint.Style.FILL);
        mBezierAreaPaint.setStrokeWidth(10);



        mBezierPath = new Path();

//        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                for (int i = 0; i < mIndicatorCount; i++) {
//                    mIndicatorList.add(new Indicator(mIndicatorRadius*(i)*2+(i+1)*mIndicatorInterval, mHeight/2));
//                }
//            }
//        });
//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                for (int i = 0; i < mIndicatorCount; i++) {
//                    Indicator indicator = new Indicator(mIndicatorRadius * (i * 2 + 1) + (i + 1) * mIndicatorInterval, mHeight / 2);
//                    mIndicatorList.add(indicator);
//                    if (i == mCurrentIndicatorPosition) {
//                        mCurrentIndicator = indicator;
//                    }
//                }
//                getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });


    }

    public void setIndicatorList(int count){
        this.mIndicatorCount = count;
        for (int i = 0; i < mIndicatorCount; i++) {
            Indicator indicator = new Indicator(mIndicatorRadius * (i * 2 + 1) + (i + 1) * mIndicatorInterval, mHeight / 2);
            mIndicatorList.add(indicator);
            if (i == mCurrentIndicatorPosition) {
                mCurrentIndicator = new Indicator(indicator.x,indicator.y);
            }
        }
        invalidate();
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
            mWidth = getPaddingLeft() + getPaddingRight() + (mIndicatorCount + 1) * (int)mIndicatorInterval + mIndicatorCount * 2 * (int)mIndicatorRadius;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = measuredHeight;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mHeight = measuredHeight + getPaddingTop() + getPaddingBottom();
        } else {
            mHeight = getPaddingTop() + getPaddingBottom() + 2 * (int)mIndicatorRadius + 10;
        }

        setMeasuredDimension(mWidth, mHeight);
        mIndicatorInterval = (mWidth - 2 * mIndicatorCount * mIndicatorRadius) / (1 + mIndicatorCount);
        mDeltDistanceBetweenIndicator = (int)mIndicatorRadius * 2 + (int)mIndicatorInterval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mIndicatorList.size(); i++) {
            Indicator indicator = mIndicatorList.get(i);
                canvas.drawCircle(indicator.x, indicator.y, mIndicatorRadius, mDefaultIndicatorPaint);
        }
        if(null != mMoveIndicator){
            canvas.drawCircle(mMoveIndicator.x,mMoveIndicator.y,mIndicatorRadius,mBezierAreaPaint);
        }
        canvas.drawCircle(mCurrentIndicator.x, mCurrentIndicator.y, mIndicatorRadius, mMoveIndicatorPaint);


        canvas.drawPath(mBezierPath,mBezierAreaPaint);
    }
    private Indicator mMoveIndicator;
    private int mLastPosition = -1;
    /**
     * @param xOffset xOffSet是一个介于0~1之间的映射两个indicator距离的值
     */
    public void indicatorMove(float xOffset) {
        if (xOffset < 0 && xOffset > 1) {
            throw new RuntimeException("offset偏移量必须介于0~1之间");
        }
        if (xOffset<0.1){
            mLastPosition = mCurrentIndicatorPosition;
        }
        if (mIndicatorList.size()>0){
            Indicator indicator = mIndicatorList.get(mLastPosition);
            float moveX = indicator.x + xOffset * mDeltDistanceBetweenIndicator;
            if (mMoveIndicator == null) {
                mMoveIndicator = new Indicator(moveX,indicator.y);
            } else {
                mMoveIndicator.x = moveX;
            }

            float middleX = (mMoveIndicator.x - indicator.x) / 2 + indicator.x;
            mBezierPath.reset();
            mBezierPath.moveTo(indicator.x, indicator.y + mIndicatorRadius);
            mBezierPath.quadTo(middleX, indicator.y, mMoveIndicator.x, mMoveIndicator.y + mIndicatorRadius);
            mBezierPath.lineTo(mMoveIndicator.x, mMoveIndicator.y - mIndicatorRadius);
            mBezierPath.quadTo(middleX, indicator.y,indicator.x, indicator.y - mIndicatorRadius);
            mBezierPath.close();

            invalidate();
        }
    }

    public void setCurrentPosition(final int position) {
        mCurrentIndicatorPosition = position;
//        mMoveIndicator = null;
        mCurrentIndicator.x = mIndicatorList.get(position).x;
        postInvalidate();
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
