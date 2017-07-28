package com.dch.test.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.dch.test.R;

/**
 * 作者：MrCoder on 2017/7/26 11:52
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class PullWaveView extends View {

    private final int RATIO = 2;
    private final int movetate = 1;
    private final int normalstate = 0;
    private final int refreshstate = 2;
    private final int refreshingstate = 3;
    private final int refreshfinish = 4;
    private final int default_line_height = 500;
    private final Point leftPoint = new Point(0, default_line_height);
    private int mScreenWidth;
    private int control = 1;
    private Paint mBallPaint;
    private Paint mAbovePaint;
    private Paint mRefreshingPaint;
    private Path mLinePath;
    private Path mAbovePath = new Path();
    private Point mPoint = new Point();
    private int CURRENT_STATE = normalstate;
    private boolean anim_running = false;

    private Point rightPoint = null;

    public PullWaveView(Context context) {
        this(context, null);
    }

    public PullWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WindowManager systemService = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = systemService.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;

        mAbovePaint = new Paint();
        mAbovePaint.setAntiAlias(true);
        mAbovePaint.setColor(Color.BLUE);
        mAbovePaint.setStyle(Paint.Style.FILL);
        mAbovePaint.setStrokeWidth(30);

        mBallPaint = new Paint();
        mBallPaint.setAntiAlias(true);
        mBallPaint.setColor(getResources().getColor(R.color.colorAccent));
        mBallPaint.setStyle(Paint.Style.FILL);
        mBallPaint.setStrokeWidth(30);
        mBallPaint.setTextSize(40);

        mPoint.x = mScreenWidth / 2;
        mPoint.y = default_line_height;

        rightPoint = new Point(mScreenWidth, default_line_height);

        mLinePath = new Path();



        mRefreshingPaint = new Paint();
        mRefreshingPaint.setAntiAlias(true);
        mRefreshingPaint.setStrokeCap(Paint.Cap.ROUND);
        mRefreshingPaint.setStyle(Paint.Style.STROKE);
        mRefreshingPaint.setColor(getResources().getColor(R.color.black_overlay));
        mRefreshingPaint.setStrokeWidth(10);
    }


    private class Point {
        public float x;
        public float y;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private float mDownY;
    private float mDownX;
    private float mDeltX;
    private float mDeltY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean onTouch = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                onTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                mDeltX = event.getX() - mDownX;
                mDeltY = (event.getY() - mDownY) / RATIO;
                if (mDeltY >= 0) {
                    mPoint.x = event.getX();
                    mPoint.y = mDeltY + default_line_height;
                    CURRENT_STATE = movetate;

                    if (mDeltY > 200) {
                        if (CURRENT_STATE != refreshstate) {

                            CURRENT_STATE = refreshstate;
                            doBallAnim();
                        }
                    } else {
                        invalidate();
                    }
                    onTouch = true;
                } else {
                    onTouch = false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mDeltY >= 0) {
                    mPoint.x = mScreenWidth / 2;
                    mPoint.y = default_line_height;
                    if (anim_running) {

                    } else {
                        CURRENT_STATE = normalstate;
                        invalidate();
                    }
                    onTouch = true;
                } else {
                    onTouch = false;
                }
                mDeltX = 0;
                mDeltY = 0;
                break;
        }

        return onTouch;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mAbovePath.reset();
        mAbovePath.moveTo(0, 0);
        mAbovePath.lineTo(0, default_line_height);
        mAbovePath.quadTo(mPoint.x, mPoint.y, rightPoint.x, rightPoint.y);
        mAbovePath.lineTo(mScreenWidth, 0);
        mAbovePath.close();
        canvas.drawPath(mAbovePath, mAbovePaint);
        if (CURRENT_STATE == refreshstate) {
            drawBall(canvas);
        }

        if (CURRENT_STATE == refreshingstate){
            drawRefreshingState(canvas);
        }

        if (CURRENT_STATE == refreshfinish){
            canvas.drawText("刷新成功",mScreenWidth/2-100,default_line_height/2,mBallPaint);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    CURRENT_STATE = normalstate;
                    postInvalidate();
                }
            },200);
        }
    }

    private void drawRefreshingState(Canvas canvas) {
        for (int i = 0; i < 12; i++) {
            mRefreshingPaint.setAlpha(((i + 1 + control) % 12 * 255) / 12);
            canvas.drawLine(mScreenWidth/2, 220, mScreenWidth/2, 200, mRefreshingPaint);
            canvas.rotate(30, mScreenWidth/2, 250);
        }
    }

    private void drawBall(Canvas canvas) {
        canvas.drawCircle(mScreenWidth / 2, animatedHeight, 30, mBallPaint);
    }

    private float animatedHeight;

    private void doBallAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(500, 150,220);
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedHeight = ((float) animation.getAnimatedValue());
                postInvalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                anim_running = false;
                CURRENT_STATE = refreshingstate;

                ValueAnimator valueAnimator = ValueAnimator.ofInt(12, 1);
                valueAnimator.setDuration(1000);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.setRepeatCount(5);
                valueAnimator.setRepeatMode(ValueAnimator.RESTART);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        control = (int) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        CURRENT_STATE = refreshfinish;
                        postInvalidate();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                    }
                });
                valueAnimator.start();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                anim_running = true;
            }
        });
        valueAnimator.start();
    }
}
