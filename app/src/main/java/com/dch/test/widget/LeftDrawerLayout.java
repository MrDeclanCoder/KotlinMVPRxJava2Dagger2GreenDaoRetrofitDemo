package com.dch.test.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by dch on 2017/6/11.
 */

public class LeftDrawerLayout extends ViewGroup {

    /**
     * drawerState: false:close, true:open
     */
    private boolean drawerState = false;
    private int screenWidth;
    private int screenHeight;
    private ViewDragHelper viewDragHelper;
    private View contentView;
    private View menuView;
    private Point menuPoint = new Point();

    public LeftDrawerLayout(Context context) {
        this(context, null);
    }

    public LeftDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        initViewDragHelper();
    }

    private void initViewDragHelper() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }

//            @Override
//            public int clampViewPositionHorizontal(View child, int left, int dx) {
//                return Math.max(-menuView.getWidth(), Math.min(left, 0));
//            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return 0;
            }

            /**
             * 手指释放时回调
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (null != menuView && releasedChild == menuView) {
                    int width = menuView.getWidth();
                    int left = menuView.getLeft();
                    if (Math.abs(left) < width * 2 / 3) {
                        if (!drawerState)
                            openDrawer();
                    } else {
                        if (drawerState)
                            closeDrawer();
                    }
                }
            }

            /**
             * 在边界拖动时回调
             * @param edgeFlags
             * @param pointerId
             */
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                if (!drawerState)
                    viewDragHelper.captureChildView(menuView, pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 0;
            }
        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    private void init(Context context, AttributeSet attrs) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //保证子孩子有两个
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("子view数目必须为2");
        }
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

//        //测量子view的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        for (int i = 0; i < childCount; i++) {
            width += getChildAt(i).getMeasuredWidth();
        }
        //规定第一个为content,第二个为menu

        setMeasuredDimension(width, screenHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (i == 0)
                childView.layout(0, 0, screenWidth, screenHeight);
            if (i == 1) {
                int childLeft = childView.getMeasuredWidth();
                childView.layout(-childLeft, 0, 0, screenHeight);
                menuPoint.x = childView.getLeft();
                menuPoint.y = childView.getTop();
            }
        }

    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!drawerState) {
            viewDragHelper.processTouchEvent(event);
        } else {
            contentView.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
        contentView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    closeDrawer();
                }
                return true;
            }
        });
    }

    public void openDrawer() {
        drawerState = true;
//        viewDragHelper.settleCapturedViewAt(menuPoint.x, menuPoint.y);
        viewDragHelper.smoothSlideViewTo(menuView, 0, 0);
//        viewDragHelper.settleCapturedViewAt(0, 0);
        invalidate();
    }

    public void closeDrawer() {
        drawerState = false;
//        viewDragHelper.settleCapturedViewAt(-menuView.getWidth(), 0);
        viewDragHelper.smoothSlideViewTo(menuView, -menuView.getWidth(), 0);
//        viewDragHelper.settleCapturedViewAt(menuPoint.x, menuPoint.y);
        invalidate();
    }
}
