package com.dch.test.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.dch.test.R;

/**
 * Created by dch on 2017/7/23.
 */

public class SwipeLayout extends ViewGroup {
    private final String TAG = "SwipeLayout";

    private TextView mTextView;
    private TextView mEditView;
    private int mLayoutWidth;
    private ViewDragHelper viewDragHelper;
    private boolean IS_OPEN = false;

    public SwipeLayout(Context context) {
        this(context,null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return mTextView ==child;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left>0){
                    left = 0;
                }
                if (left<-300){
                    left = -300;
                }
                return left;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 300;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 0;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (mTextView.getLeft()>-150){
                    viewDragHelper.smoothSlideViewTo(mTextView,0,0);
                    IS_OPEN = false;
                } else {
                    viewDragHelper.smoothSlideViewTo(mTextView,-300,0);
                    IS_OPEN = true;
                }
                invalidate();
            }

        });
        init(context,attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditView = (TextView) findViewById(R.id.tv_edit_layout);
        mEditView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IS_OPEN){
                    Log.d(TAG,"点击了");
                    if (listener != null) {
                        listener.onMenuClick(v);
                    }
                }
            }
        });
        mTextView = (TextView) findViewById(R.id.tv_edit_text_layout);
    }

    private void init(Context context, AttributeSet attrs) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        mLayoutWidth = displayMetrics.widthPixels;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measuredWidth + 300,MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mTextView.layout(0,0,mLayoutWidth,300);
        mEditView.layout(mLayoutWidth-300,0,mLayoutWidth,300);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            viewDragHelper.cancel();
            return false;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public interface OnMenuClickListener {
        void onMenuClick(View v);
    }

    public OnMenuClickListener listener;

    public void setOnMenuClickListener(OnMenuClickListener clickListener) {
        this.listener = listener;
    }
}
