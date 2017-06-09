package com.dch.test.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.dch.test.R;

/**
 * 自定义音量view
 * Created by dch on 2017/6/9.
 */
public class CustomVolumControlBar extends View {

    private int firstColor;
    private int secondColor;
    private int dotCount;
    private int mCurrentCount = 3; //默认大小是3
    private Bitmap bitmap;
    private int splitSize;
    private int circleWidth;
    private int mCenterAngel;

    private Paint mPaint;
    private Rect mRect;

    private int defaultCircleWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    public CustomVolumControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumControlBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumControlBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomVolumControlBar_firstcolor:
                    firstColor = typedArray.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.CustomVolumControlBar_secondcolor:
                    secondColor = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomVolumControlBar_centerimg:
                    bitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomVolumControlBar_circlewidth:
                    circleWidth = typedArray.getDimensionPixelSize(typedArray.getIndex(attr), defaultCircleWidth);
                    break;
                case R.styleable.CustomVolumControlBar_dotcount:
                    dotCount = typedArray.getInt(attr, 8);
                    break;
                case R.styleable.CustomVolumControlBar_splitsize:
                    splitSize = typedArray.getInt(attr, 10);
                    break;
                case R.styleable.CustomVolumControlBar_centerangel:
                    mCenterAngel = typedArray.getInt(attr, 360);//默认是原形,小于360则是圆弧
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        mPaint.setStrokeCap(Paint.Cap.ROUND); //断点出圆形
        mPaint.setStrokeWidth(circleWidth); //设置圆环的宽度
        mRect = new Rect();
    }
/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int desireWidth = widthSize;
        if(null != bitmap ){
            desireWidth = getPaddingLeft()+getPaddingRight()+getWidth()+bitmap.getWidth();
        }

        switch (widthMode){
            case MeasureSpec.EXACTLY:// match_parent or 指定尺寸
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST://wrap_content
                width = Math.min(widthSize,desireWidth);
                break;
            case MeasureSpec.UNSPECIFIED:
                //父控件对子控件不加任何束缚，子元素可以得到任意想要的大小，这种MeasureSpec一般是由父控件自身的特性决定的。
                //比如ScrollView，它的子View可以随意设置大小，无论多高，都能滚动显示，这个时候，尺寸就选择自己需要的尺寸size。
            default:
                width =widthSize;
                break;
        }
        //正方形
        setMeasuredDimension(width,width);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int center = getWidth() / 2; //中心点
        int radius = center - circleWidth / 2; //半径

        drawOvals(canvas, center, radius);

        //计算内切正方形的rect
        int innerRadius = radius - circleWidth;//内切圆的半径
        mRect.left = circleWidth + (int) (innerRadius - Math.sqrt(2.0f) / 2 * innerRadius)+20;
        mRect.top = mRect.left;
        mRect.right = radius + (int) ( Math.sqrt(2.0f) * innerRadius)/2-20;
        mRect.bottom = mRect.right;

        /**
         * 如果图片比较小，那么根据图片的尺寸放置到正中心
         */
        if (bitmap.getWidth() < Math.sqrt(2) * innerRadius) {
            mRect.left = mRect.left + (int) Math.sqrt(2) / 2 * innerRadius - bitmap.getWidth() / 2;
            mRect.top = mRect.top + (int) Math.sqrt(2) / 2 * innerRadius - bitmap.getHeight() / 2;
            mRect.right = mRect.left + bitmap.getWidth()+ (int) Math.sqrt(2) / 2 * innerRadius;
            mRect.bottom = mRect.top + bitmap.getHeight()+ (int) Math.sqrt(2) / 2 * innerRadius;
        }
        //绘图
        canvas.drawBitmap(bitmap, null, mRect, mPaint);
    }

    private void drawOvals(Canvas canvas, int center, int radius) {
        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius); //用于定义圆弧的形状和大小的界限

        int splitCount = dotCount; // 分割块的个数 默认为圆弧分割的块数

        /**
         * 圆弧起始角度
         * 此处为左下角135度
         */
        int startAngel = 135 + splitSize / 2;
        if (mCenterAngel < 360 && mCenterAngel > 0) {
            splitCount--;
            startAngel = 270 - mCenterAngel / 2;
        } else {
            mCenterAngel = 360;
        }

        //根据需要画的个数以及间隙计算每个进度块的长度（以圆周长360为基准）
        float itemSize = (mCenterAngel * 1.0f - splitCount * splitSize) / dotCount;

        mPaint.setColor(firstColor);
        for (int i = 0; i < dotCount; i++) {
            canvas.drawArc(rectF, startAngel + i * (splitSize + itemSize), itemSize, false, mPaint);
        }

        mPaint.setColor(secondColor);
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(rectF, startAngel + i * (splitSize + itemSize), itemSize, false, mPaint);
        }
    }

    private void up() {
        if (mCurrentCount < dotCount) {
            mCurrentCount++;
            invalidate();
        }
    }

    private void down() {
        if (mCurrentCount > 0) {
            mCurrentCount--;
            invalidate();
        }
    }

    private int downY, moveY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                moveY = (int) event.getY();
                int deltY = moveY - downY;
//                deltY = deltY -  20;
                if (deltY > 0) {
                    down();
                } else if ( deltY < 0){
                    up();
                }

                if (Math.abs(moveY) > 0){
                    downY = moveY;
                }
                break;
        }
        return true;
    }
}
