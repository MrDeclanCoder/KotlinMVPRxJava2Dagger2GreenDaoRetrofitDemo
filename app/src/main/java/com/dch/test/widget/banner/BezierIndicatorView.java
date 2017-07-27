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
public class BezierIndicatorView extends View {
    private int mDefaultIndicatorColor = Color.LTGRAY;
    private int mMoveIndicatorColor = Color.RED;
    private int mIndicatorCount = 5;
    private int mIndicatorRadius = 15;
    private int mIndicatorInterval = 10;
    private int mWidth;
    private int mHeight;
    private int mCurrentIndicatorPosition = 0;
    private List<Indicator> mIndicatorList = new ArrayList<>();
    private Path mBezierPath;
    private Paint mDefaultIndicatorPaint;
    private Paint mMoveIndicatorPaint;

    public BezierIndicatorView(Context context) {
        this(context, null);
    }

    public BezierIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultIndicatorPaint.setColor(mDefaultIndicatorColor);
        mDefaultIndicatorPaint.setStyle(Paint.Style.FILL);
        mDefaultIndicatorPaint.setStrokeWidth(10);

        mMoveIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMoveIndicatorPaint.setColor(mMoveIndicatorColor);
        mMoveIndicatorPaint.setStyle(Paint.Style.FILL);
        mMoveIndicatorPaint.setStrokeWidth(10);

        mBezierPath = new Path();

        for (int i = 0; i < mIndicatorCount; i++) {
            mIndicatorList.add(new Indicator(mIndicatorRadius+(i+1)*mIndicatorInterval, mHeight/2));
        }
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
            mWidth = getPaddingLeft() + getPaddingRight() + (mIndicatorCount + 1) * mIndicatorInterval + mIndicatorCount * 2 * mIndicatorRadius;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = measuredHeight;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mHeight = measuredHeight + getPaddingTop() + getPaddingBottom();
        } else {
            mHeight = getPaddingTop() + getPaddingBottom() + 2 * mIndicatorRadius + 10;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Indicator indicator : mIndicatorList) {
            canvas.drawCircle(indicator.x,indicator.y,mIndicatorRadius,mDefaultIndicatorPaint);
        }
    }

    private class Indicator{
        public int x = -1;
        public int y = -1;

        public Indicator(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}
