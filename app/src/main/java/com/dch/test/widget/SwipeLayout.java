package com.dch.test.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dch.test.R;

/**
 * Created by dch on 2017/7/23.
 */

public class SwipeLayout extends ViewGroup {
    private final String TAG = "SwipeLayout";

    private TextView mTextView;
    private ImageView mEditView;
    private int mLayoutWidth;
    private int mMeasuredHeight = 0;
    private int mMenuItemCount = 1;
    private int mMenuItemWidth;
    private int mMenuWidth = 0;
    private ViewDragHelper viewDragHelper;
    private boolean IS_OPEN = false;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        intViewDragHelper();
        init(context);
    }

    private void intViewDragHelper() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return mTextView == child;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {

                if (left < -mMenuItemCount * mMenuItemWidth) {
                    left = -mMenuItemCount * mMenuItemWidth;
                } else {
                    if (left > 0) {
                        left = 0;
                    }
                }
                return left;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mMenuItemCount * mMenuItemWidth;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 0;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (IS_OPEN) {
                    if (mTextView.getLeft() > -mMenuWidth * 4 / 5) {
                        setMenuEnabled(false);
                    } else {
                        setMenuEnabled(true);
                    }
                } else {
                    if (mTextView.getLeft() > -mMenuWidth / 5) {
                        setMenuEnabled(false);
                    } else {
                        setMenuEnabled(true);
                    }
                }
            }
        });
    }

    private void setMenuEnabled(boolean enable) {
        IS_OPEN = enable;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setClickable(enable);
        }
        if (enable) {
            viewDragHelper.smoothSlideViewTo(mTextView, -mMenuWidth, 0);
        } else {
            viewDragHelper.smoothSlideViewTo(mTextView, 0, 0);
        }
        invalidate();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeLayout);
        int indexCount = attrs.getAttributeCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SwipeLayout_menuItemCount:
                    mMenuItemCount = typedArray.getInteger(attr, 1);
                    break;
                case R.styleable.SwipeLayout_menuItemWidth:
                    mMenuItemWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
                    break;
            }

        }
        typedArray.recycle();
        mMenuWidth = mMenuItemWidth * mMenuItemCount;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditView = (ImageView) getChildAt(0);
        mEditView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IS_OPEN) {
                    if (listener != null) {
                        listener.onMenuClick(v);
                    }
                }
            }
        });
        mTextView = (TextView) getChildAt(1);
        setMenuEnabled(false);
        mTextView.setGravity(Gravity.CENTER);
    }

    private void init(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        mLayoutWidth = displayMetrics.widthPixels;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMeasuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measuredWidth + mMenuItemWidth * mMenuItemCount, mMeasuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //规定第一个为item布局,以后的为menu布局中item
        if (0 == mMeasuredHeight) {
            mMeasuredHeight = 180;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == childCount - 1) {
                getChildAt(i).layout(0, 0, mLayoutWidth, mMeasuredHeight);
            } else {
                getChildAt(i).layout(mLayoutWidth - (mMenuItemCount - i) * mMenuItemWidth, 0, mLayoutWidth - mMenuItemWidth * (mMenuItemCount - i - 1), mMeasuredHeight);
            }
        }
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
        this.listener = clickListener;
    }
}
