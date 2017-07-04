package com.dch.test.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dch.test.R;

/**
 * 作者：MrCoder on 2017/7/4 15:02
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class CustomProgressBar extends View {
    private String content = "跳过";
    private int mMaxProgress = 100;
    private int mCircleWidth = 3;
    private int mTextSize = 45;
    private int mBackgroundWidth = 100;
    private int mCircleColor = 0xff0000;
    private int mTextColor = 0xf0ffff;
    private int mCurrentProgress = 0;
    private int mStartAngel = 0;
    private int mBackgroundColor = 0xcfcfcf;
    private RectF rectF;
    private Paint mBgPaint;
    private Paint mProgressPaint;
    private Paint mTextPaint;

    private int swipeAngel = 0;
    private Rect mTextRect;
    private float mTextY;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomProgressBar_maxProress:
                    mMaxProgress = typedArray.getInt(attr, 100);
                    break;
                case R.styleable.CustomProgressBar_circleColor:
                    mCircleColor = typedArray.getColor(attr, 0xff0000);
                    break;
                case R.styleable.CustomProgressBar_textColor:
                    mTextColor = typedArray.getColor(attr, 0xff0000);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    mCircleWidth = typedArray.getDimensionPixelSize(attr, 3);
                    break;
                case R.styleable.CustomProgressBar_backgroundWidth:
                    mBackgroundWidth = typedArray.getDimensionPixelSize(attr, 10);
                    break;
                case R.styleable.CustomProgressBar_currentProgress:
                    mCurrentProgress = typedArray.getInt(attr, 0);
                    break;
                case R.styleable.CustomProgressBar_backgroundColor:
                    mBackgroundColor = typedArray.getColor(attr, 0xcfcfcf);
                    break;
                case R.styleable.CustomProgressBar_textSize:
                    mTextSize = typedArray.getDimensionPixelSize(attr, 45);
                    break;
            }
        }
        typedArray.recycle();
        mBgPaint = new Paint();
        mBgPaint.setColor(mBackgroundColor);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStrokeWidth(mBackgroundWidth / 2);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(mCircleColor);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mCircleWidth);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextRect = new Rect();
        mTextPaint.getTextBounds(content, 0, content.length(), mTextRect);

        mTextY = mTextPaint.descent() - mTextPaint.ascent();

        rectF = new RectF(5, 5, mBackgroundWidth - 5, mBackgroundWidth - 5);
        mStartAngel = mCurrentProgress * 360 / 100 - 90;

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(mBackgroundWidth, mBackgroundWidth);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float baseLine = getMeasuredHeight() / 2 + mTextPaint.getTextSize() / 2 - mTextPaint.getFontMetrics().descent;
        //画背景
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBackgroundWidth / 2, mBgPaint);
        //画进度
        canvas.drawArc(rectF, mStartAngel, swipeAngel, false, mProgressPaint);

        canvas.drawText(content, getWidth() / 2 - mTextRect.width() / 2, baseLine, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (listener != null) listener.onClick();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setProgress(int progress) {
        if (progress > 100) progress = 100;
        if (progress < 0) progress = 0;
        swipeAngel = progress * 360 / mMaxProgress;
        invalidate();
    }

    public void start() {
        mCurrentProgress--;
        if (mCurrentProgress < 0) mCurrentProgress = 0;
        swipeAngel = mCurrentProgress * 360 / mMaxProgress;
        invalidate();
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public OnClickListener listener;

}
