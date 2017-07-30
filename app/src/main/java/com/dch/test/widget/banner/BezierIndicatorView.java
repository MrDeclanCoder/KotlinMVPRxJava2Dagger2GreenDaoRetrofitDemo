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
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：MrCoder on 2017/7/27 17:15
 * 描述：自定义贝塞尔效果指示器indicator
 * 邮箱：daichuanhao@caijinquan.com
 */
public class BezierIndicatorView extends View {
    private final String TAG = "BezierIndicatorView";

    private final float RATIO = 0.551915024494f;
    Point indicator;
    private int mDefaultIndicatorColor = Color.BLUE;
    private int mMoveIndicatorColor = Color.RED;
    private int mIndicatorCount = 5;
    private float mOffSet = 0;
    private float mIndicatorRadius = 15;
    float delt = mIndicatorRadius * RATIO;
    private float mIndicatorInterval = 10;
    private int mWidth;
    private int mHeight;
    private int mCurrentIndicatorPosition = 0;
    private float mDeltDistanceBetweenIndicator = 0;
    private Indicator mCurrentIndicator;
    private List<Indicator> mIndicatorList = new LinkedList<>();
    private Path mBezierPath ;
    private Path mCurrentPath = new Path();
    private List<Path> mDefaultIndicatorPaths = new LinkedList<>();
    private List<Path> mCirclePaths = new ArrayList<>();
    private Paint mDefaultIndicatorPaint;
    private Paint mMoveIndicatorPaint;
    private Paint mCircleLinePaint;
    private int mLastPosition = -1;
    private Path mPrePath;
    private Path mNextPath;
    private Indicator mNextIndicator;
    private Indicator mPreIndicator;

    private float x1;
    private float x2;
    private float x3;
    private float x4;
    private float x5;
    private float x6;
    private float x7;
    private float x8;



    private Indicator indicator1;
    private Indicator indicator2;
    private Indicator indicator3;
    private Indicator indicator4;
    private Indicator indicator5;

    private Indicator indicator11;
    private Indicator indicator22;
    private Indicator indicator33;
    private Indicator indicator44;
    private Indicator indicator55;


    private Path path1 = new Path();
    private Path path2 = new Path();
    private Path path3 = new Path();
    private Path path4 = new Path();
    private Path path5 = new Path();

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

        mCircleLinePaint = new Paint();
        mCircleLinePaint.setAntiAlias(true);
        mCircleLinePaint.setColor(mDefaultIndicatorColor);
        mCircleLinePaint.setStyle(Paint.Style.STROKE);
        mCircleLinePaint.setStrokeWidth(mIndicatorRadius * 2);


    }

    public void setIndicatorList(int count) {
        if (mDefaultIndicatorPaths.size()>0) mDefaultIndicatorPaths.clear();

        this.mIndicatorCount = count;
        for (int i = 0; i < mIndicatorCount; i++) {

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

            mIndicatorList.add(indicator);

            mDefaultIndicatorPaths.add(calculatePath(indicator));
            if (i == mCurrentIndicatorPosition) {
                mCurrentIndicator = indicator;
                mCurrentPath = calculatePath(indicator);
            }
        }
        x1 = mIndicatorRadius * 2 + mIndicatorInterval;
        x2 = x1+ mIndicatorInterval;
        x3 = x2 + mIndicatorRadius*2;
        x4 = x3 + mIndicatorInterval;
        x5 = x4 + mIndicatorRadius*2;
        x6 = x5 + mIndicatorInterval;
        x7 = x6 + mIndicatorRadius*2;
        x8 = x7 + mIndicatorInterval;
        mCirclePaths = mDefaultIndicatorPaths;
        invalidate();
    }

    public void setIndicatorList2(int count){
        if (mDefaultIndicatorPaths.size()>0) mDefaultIndicatorPaths.clear();
        this.mIndicatorCount = count;
        indicator1 = generateIndicator(0);
        indicator11 = generateIndicator(0);
        indicator2 = generateIndicator(1);
        indicator22 = generateIndicator(1);
        indicator3 = generateIndicator(2);
        indicator33 = generateIndicator(2);
        indicator4 = generateIndicator(3);
        indicator44 = generateIndicator(3);
        indicator5 = generateIndicator(4);
        indicator55 = generateIndicator(4);

        path1 = calculatePath(indicator1);
        path2 = calculatePath(indicator2);
        path3 = calculatePath(indicator3);
        path4 = calculatePath(indicator4);
        path5 = calculatePath(indicator5);

        mDefaultIndicatorPaths.add(path1);
        mDefaultIndicatorPaths.add(path2);
        mDefaultIndicatorPaths.add(path3);
        mDefaultIndicatorPaths.add(path4);
        mDefaultIndicatorPaths.add(path5);

        mCurrentIndicator = indicator1;
        mCurrentPath = calculatePath(indicator1);
        mCirclePaths = mDefaultIndicatorPaths;
        invalidate();
    }
    private Indicator generateIndicator(int i){
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

    private Path calculatePath(Indicator indicator) {
        Path mBezierPath = new Path();
        mBezierPath.reset();
        mBezierPath.moveTo(indicator.point0.x, indicator.point0.y);
        mBezierPath.cubicTo(indicator.point1.x, indicator.point1.y, indicator.point2.x, indicator.point2.y, indicator.point3.x, indicator.point3.y);
        mBezierPath.cubicTo(indicator.point4.x, indicator.point4.y, indicator.point5.x, indicator.point5.y, indicator.point6.x, indicator.point6.y);
        mBezierPath.cubicTo(indicator.point7.x, indicator.point7.y, indicator.point8.x, indicator.point8.y, indicator.point9.x, indicator.point9.y);
        mBezierPath.cubicTo(indicator.point10.x, indicator.point10.y, indicator.point11.x, indicator.point11.y, indicator.point0.x, indicator.point0.y);
        mBezierPath.close();
        return mBezierPath;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        for (int i = 0; i < mDefaultIndicatorPaths.size(); i++) {
//            canvas.drawPath(mDefaultIndicatorPaths.get(i), mDefaultIndicatorPaint);
//        }

        canvas.drawPath(path1, mDefaultIndicatorPaint);
        canvas.drawPath(path2, mDefaultIndicatorPaint);
        canvas.drawPath(path3, mDefaultIndicatorPaint);
        canvas.drawPath(path4, mDefaultIndicatorPaint);
        canvas.drawPath(path5, mDefaultIndicatorPaint);


        canvas.drawPath(mCurrentPath,mMoveIndicatorPaint);

    }

    /**
     * @param xOffset xOffSet是一个介于0~1之间的映射两个indicator距离的值
     */
    public void indicatorMove(float xOffset) {
        if (xOffset < 0 && xOffset > 1) {
            throw new RuntimeException("offset偏移量必须介于0~1之间");
        }
        if (xOffset < 0.1) {
            mLastPosition = mCurrentIndicatorPosition;
        }
        if (mIndicatorList.size() > 0) {
            this.mOffSet = xOffset;
            mPreIndicator = mIndicatorList.get(mLastPosition);
            mPreIndicator.point2.x = mIndicatorRadius * (mLastPosition * 2 + 2) + (mLastPosition + 1) * mIndicatorInterval + mIndicatorInterval * mOffSet;
            mPreIndicator.point3.x = mPreIndicator.point2.x;
            mPreIndicator.point4.x = mPreIndicator.point2.x;

            mNextIndicator = mIndicatorList.get(mLastPosition+1);
            mNextIndicator.point8.x = mIndicatorRadius * (mLastPosition+1) * 2 + (mLastPosition+1 + 1) * mIndicatorInterval - mIndicatorInterval * mOffSet;
            mNextIndicator.point9.x = mNextIndicator.point8.x;
            mNextIndicator.point10.x = mNextIndicator.point8.x;


            mPrePath = calculatePath(mPreIndicator);
            mDefaultIndicatorPaths.remove(mLastPosition);
            mDefaultIndicatorPaths.add(mLastPosition, mPrePath);

            mNextPath = calculatePath(mNextIndicator);
            mDefaultIndicatorPaths.remove(mLastPosition+1);
            mDefaultIndicatorPaths.add(mLastPosition+1, mNextPath);

            invalidate();
        }
    }

    /**
     * @param xOffset xOffSet是一个介于0~1之间的映射两个indicator距离的值
     */
    public void indicatorMove2(float xOffset) {
        if (xOffset < 0 && xOffset > 1) {
            throw new RuntimeException("offset偏移量必须介于0~1之间");
        }
if (!flag){
    return;
}
        if (indicator1 != null) {
            if (xOffset < 0.1) {
                mLastPosition = mCurrentIndicatorPosition;
            }
            this.mOffSet = xOffset;

            if (mLastPosition == 0){
                indicator1.point2.x = indicator11.point2.x+ mIndicatorInterval * mOffSet;
                indicator1.point3.x = indicator11.point2.x+ mIndicatorInterval * mOffSet;
                indicator1.point4.x = indicator11.point2.x+ mIndicatorInterval * mOffSet;

                path1 = calculatePath(indicator1);

                indicator2.point8.x = indicator22.point8.x - mIndicatorInterval * mOffSet;
                indicator2.point9.x = indicator22.point8.x - mIndicatorInterval * mOffSet;
                indicator2.point10.x = indicator22.point8.x - mIndicatorInterval * mOffSet;

                path2 = calculatePath(indicator2);

            } else if(mLastPosition == 1){
                indicator2.point2.x = indicator22.point2.x + mIndicatorInterval * mOffSet;
                indicator2.point3.x = indicator22.point2.x + mIndicatorInterval * mOffSet;
                indicator2.point4.x = indicator22.point2.x + mIndicatorInterval * mOffSet;

                path2 = calculatePath(indicator2);

                indicator3.point8.x = indicator33.point8.x - mIndicatorInterval * mOffSet;
                indicator3.point9.x = indicator33.point8.x - mIndicatorInterval * mOffSet;
                indicator3.point10.x = indicator33.point8.x - mIndicatorInterval * mOffSet;

                path3 = calculatePath(indicator3);
            } else if(mLastPosition == 2){
                indicator3.point2.x = indicator33.point2.x + mIndicatorInterval * mOffSet;
                indicator3.point3.x = indicator33.point2.x + mIndicatorInterval * mOffSet;
                indicator3.point4.x = indicator33.point2.x + mIndicatorInterval * mOffSet;

                path3 = calculatePath(indicator3);

                indicator4.point8.x = indicator44.point8.x - mIndicatorInterval * mOffSet;
                indicator4.point9.x = indicator44.point8.x - mIndicatorInterval * mOffSet;
                indicator4.point10.x = indicator44.point8.x - mIndicatorInterval * mOffSet;

                path4 = calculatePath(indicator4);
            } else if(mLastPosition == 3){
                indicator4.point2.x = indicator44.point2.x + mIndicatorInterval * mOffSet;
                indicator4.point3.x = indicator44.point2.x + mIndicatorInterval * mOffSet;
                indicator4.point4.x = indicator44.point2.x + mIndicatorInterval * mOffSet;

                path4 = calculatePath(indicator4);

                indicator5.point8.x = indicator55.point8.x - mIndicatorInterval * mOffSet;
                indicator5.point9.x = indicator55.point8.x - mIndicatorInterval * mOffSet;
                indicator5.point10.x = indicator55.point8.x - mIndicatorInterval * mOffSet;

                path5 = calculatePath(indicator5);
            } else if(mLastPosition == 4){
                indicator5.point2.x = indicator55.point2.x + mIndicatorInterval * mOffSet;
                indicator5.point3.x = indicator55.point2.x + mIndicatorInterval * mOffSet;
                indicator5.point4.x = indicator55.point2.x + mIndicatorInterval * mOffSet;

                path5 = calculatePath(indicator5);

                indicator4.point8.x = indicator44.point8.x - mIndicatorInterval * mOffSet;
                indicator4.point9.x = indicator44.point8.x - mIndicatorInterval * mOffSet;
                indicator4.point10.x = indicator44.point8.x - mIndicatorInterval * mOffSet;

                path4 = calculatePath(indicator4);
            }

            invalidate();
        }
    }

    private boolean flag = true;
    public void setCurrentPosition(final int position) {
        flag = false;
                mCurrentIndicatorPosition = position;
                path1 = mCirclePaths.get(0);
                path2 = mCirclePaths.get(1);
                path3 = mCirclePaths.get(2);
                path4 = mCirclePaths.get(3);
                path5 = mCirclePaths.get(4);

                mCurrentPath = mCirclePaths.get(mCurrentIndicatorPosition);
                invalidate();
        flag = true;
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
