package com.dch.test.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.dch.test.R;
import com.dch.test.util.UIUtils;

/**
 * 作者：MrCoder on 2017/7/10 14:25
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class WaterRefreshView extends ScrollView {

    private WaterView mWaterView;
    private LinearLayout mContainer;
    private View headerView;
    private int MIN_DRAG_HEIGHT ;
    private int RELEASE_REFRESH_HEIGHT ;
    private int mCurrentState = STATE_DEFAULT;
    private static final int STATE_DEFAULT = 0X001;
    private static final int STATE_MOVE = 0X002;
    private static final int STATE_RELEASE = 0X003;
    private static final int STATE_REFRESH = 0X004;
    private static final float RATIO_MOVE = 2.6f;
    private ProgressBar mPorgressBar;

    public WaterRefreshView(Context context) {
        this(context, null);
    }

    public WaterRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View inflateChildView = getChildAt(0);
        removeAllViews();
        mContainer.addView(inflateChildView);
        addView(mContainer, 0);
    }

    private void init(Context context) {
        RELEASE_REFRESH_HEIGHT = UIUtils.dip2px(100,context);
        MIN_DRAG_HEIGHT = UIUtils.dip2px(37,context);
        mContainer = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.setLayoutParams(layoutParams);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        headerView = LayoutInflater.from(context).inflate(R.layout.refresh_header_waterview, null);
        mContainer.addView(headerView, 0);
        mWaterView = (WaterView) headerView.findViewById(R.id.waterview_head);
        mPorgressBar = ((ProgressBar) headerView.findViewById(R.id.pb_refresh_water_head));
        mPorgressBar.setVisibility(GONE);
        headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updateMargin(headerView.getHeight());
                headerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


    }

    private void updateMargin(int topMargin) {
        LinearLayout.LayoutParams headerViewLayoutParams = (LinearLayout.LayoutParams) headerView.getLayoutParams();
        headerViewLayoutParams.setMargins(0, -topMargin, 0, 0);
        headerView.setLayoutParams(headerViewLayoutParams);
    }

    private float downY;
    private float deltY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                deltY = ev.getY() - downY;
                if (getScrollY() == 0 && mCurrentState != STATE_REFRESH && deltY > 0) {
                    deltY = deltY/RATIO_MOVE;
                    calculatorDeltY(deltY);
                    return true;
                } else {
                    return super.onTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                if (getScrollY() == 0 && deltY > RELEASE_REFRESH_HEIGHT) {
                    if (mCurrentState != STATE_REFRESH) {
                        setRefreshState();
                    }
                    deltY = 0;
                    return true;
                } else {
                    deltY = 0;
                    resetHeaderState();
                    return super.onTouchEvent(ev);
                }
        }
        return super.onTouchEvent(ev);
    }

    public class RefreshRunnable implements Runnable {

        @Override
        public void run() {
            if (listener != null) {
                mCurrentState = STATE_REFRESH;
                listener.onRefresh();
                mPorgressBar.setVisibility(VISIBLE);
                mWaterView.setVisibility(GONE);
            } else {
                refreshSuccess();
            }
        }
    }

    private void calculatorDeltY(float deltY) {
        updateHeaderMargin(deltY);
        if (deltY > MIN_DRAG_HEIGHT && deltY < RELEASE_REFRESH_HEIGHT) {
            Log.d("aaa", "calculatorDeltY: " + (deltY - MIN_DRAG_HEIGHT));
            setMoveState(deltY);
        } else if (deltY >= RELEASE_REFRESH_HEIGHT) {
            setReleaseToRefreshState();
        }
    }

    private void setReleaseToRefreshState() {
        mWaterView.setGatherState();

    }

    private void setRefreshState(){
        postDelayed(new RefreshRunnable(), 500);
        if (deltY > RELEASE_REFRESH_HEIGHT) {
            scrollHeader(-((int) deltY - headerView.getHeight()), 0);
        }
    }

    private void scrollHeader(int start, int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                updateMargin(animatedValue);
            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }

    public void setMoveState(float deltY) {
        mWaterView.setMoveState((int) (deltY - MIN_DRAG_HEIGHT));
    }

    private void updateHeaderMargin(float deltY) {
        updateMargin(headerView.getHeight() - (int) deltY);
    }

    public void refreshSuccess() {
        resetHeaderState();
    }

    public void refreshFailed() {
        resetHeaderState();
    }

    private void resetHeaderState() {
        if (mWaterView != null) {
            mWaterView.resetState();
            mWaterView.setVisibility(VISIBLE);
            mPorgressBar.setVisibility(GONE);
        }
        mCurrentState = STATE_DEFAULT;
        LinearLayout.LayoutParams headerViewLayoutParams = (LinearLayout.LayoutParams) headerView.getLayoutParams();
        int topMargin = headerViewLayoutParams.topMargin;
        scrollHeader(-(topMargin), headerView.getHeight());
    }

    OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
