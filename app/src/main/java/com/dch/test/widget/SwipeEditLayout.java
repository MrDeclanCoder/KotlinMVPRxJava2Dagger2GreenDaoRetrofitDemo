package com.dch.test.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dch.test.R;

/**
 * 作者：MrCoder on 2017/7/21 15:55
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class SwipeEditLayout extends RelativeLayout {
    private final String TAG = "SwipeEditLayout";

    private TextView mTextView;
    private TextView mEditView;
    private int mLayoutWidth;
    private int mLayoutHeight;
    private ObjectAnimator translationX;
    private final int STATE_ON = 0;
    private final int STATE_OFF = 1;
    private int CURRENSTSTE = STATE_OFF;
    private boolean isLeft = false;
    private boolean isRight = true;

    public SwipeEditLayout(Context context) {
        this(context, null);
    }

    public SwipeEditLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeEditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View editLayout = LayoutInflater.from(context).inflate(R.layout.edit_layout, null);
        mEditView = (TextView) editLayout.findViewById(R.id.bt_edit_layout);
        mEditView.setText("button");
        mEditView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"点击了");
                if (listener != null) {
                    listener.onMenuClick(v);
                }
            }
        });
        mTextView = (TextView) editLayout.findViewById(R.id.tv_edit_layout);
        mTextView.setText("我是内容");
        addView(editLayout);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLayoutWidth = getWidth();
                mLayoutHeight = getHeight();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private float mDownX;
    private float mDeltX;
    private VelocityTracker xVelocityTracker;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
//                if (xVelocityTracker != null) {
//                    xVelocityTracker.clear();
//                } else {
//                    xVelocityTracker = VelocityTracker.obtain();
//                }
//                xVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
//                xVelocityTracker.addMovement(event);
//                xVelocityTracker.computeCurrentVelocity(1000);
                mDeltX = event.getX() - mDownX;
                Log.d(TAG, String.valueOf(mDeltX));
                if (mDeltX < 0) {
//                    if (!isLeft) {
                    if (mTextView.getLeft()>-mLayoutHeight){
                        if (Math.abs(mDeltX) < mLayoutHeight) {
                            mTextView.layout((int) mDeltX, 0, mLayoutWidth - (int) Math.abs(mDeltX), mLayoutHeight);
                        } else {
                            mTextView.layout(-mLayoutHeight, 0, mLayoutWidth - mLayoutHeight, mLayoutHeight);
                        }
                        postInvalidate();
                    }

//                    }
                }
                if (mDeltX > 0) {
//                    if (isLeft) {
                    if (mTextView.getLeft()<0){
                        if (mDeltX < mLayoutHeight) {
                            mTextView.layout((int) mDeltX - mLayoutHeight, 0, mLayoutWidth + (int) Math.abs(mDeltX) - mLayoutHeight, mLayoutHeight);
                        } else if (mDeltX >= mLayoutHeight) {
                            mTextView.layout(0, 0, mLayoutWidth, mLayoutHeight);
                        }
                        postInvalidate();
                    }
//                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mDeltX < 0) {
                            mTextView.layout(-mLayoutHeight, 0, mLayoutWidth-mLayoutHeight, mLayoutHeight);
                } else if (mDeltX > 0) {
                        mTextView.layout(0, 0, mLayoutWidth, mLayoutHeight);
                }
                mDeltX = 0;
                break;
        }


        return true;
    }
    //                                translationX = ObjectAnimator.ofFloat(mTextView, "translationX", Math.abs(mDeltX) - mLayoutHeight);
//                                translationX.setDuration(300);
//                                translationX.setInterpolator(new DecelerateInterpolator());
//                                translationX.start();
//                                translationX.addListener(new Animator.AnimatorListener() {
//                                    @Override
//                                    public void onAnimationStart(Animator animation) {
//
//                                    }
//
//                                    @Override
//                                    public void onAnimationEnd(Animator animation) {
//                                        CURRENSTSTE = STATE_ON;
//                                    }
//
//                                    @Override
//                                    public void onAnimationCancel(Animator animation) {
//
//                                    }
//
//                                    @Override
//                                    public void onAnimationRepeat(Animator animation) {
//
//                                    }
//                                });
    public interface OnMenuClickListener {
        void onMenuClick(View v);
    }

    public OnMenuClickListener listener;

    public void setOnMenuClickListener(OnMenuClickListener clickListener) {
        this.listener = listener;
    }
}
