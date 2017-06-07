package com.dch.test.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dch.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：MrCoder on 2017/6/7 14:13
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class FiveChessesView extends View {
    private Bitmap mWhiteChess;//白棋图片
    private Bitmap mBlackChess;//黑棋图片
    private int mPanelWidth;//棋盘宽度
    private float mLineHeight;//每一格的高度
    private final static int MAX_LINES = 10;//棋盘多少格（10*10）
    private Paint mPaint;//画笔
    private float mChessRatio = 3.0f / 4;//设置棋子大小
    private ArrayList<Point> mBlackList = new ArrayList<>();//黑棋子数据
    private ArrayList<Point> mWhiteList = new ArrayList<>();//白棋子数据boo
    private boolean isWhiteFirst = false;//是否先出白棋
    private boolean isBlackWin = false;//黑棋子赢
    private boolean isWhiteWin = false;//白棋子赢
    private boolean isGameOver = false;//游戏是否结束
    private final static int LINE_MAX_NUMS = 5;//同色五子成线表示结束
    //以下几个常量是为了保存界面数据
    private final static String GOBANG_INSTANCE = "gobang_instance";
    private final static String GAME_OVER = "game_over";
    private final static String WHITE_DATA = "white_data";
    private final static String BLACK_DATA = "black_data";

    public interface IChessResultListener {
        void ResultListerer(int result);
    }

    private IChessResultListener mListener;

    public void setIChessResultListener(IChessResultListener mListener) {
        this.mListener = mListener;
    }

    public FiveChessesView(Context context) {
        this(context, null);
    }

    public FiveChessesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FiveChessesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0x44ff0000);//设置背景色
        initPaintAndBitmap();

    }

    /**
     * 初始化及设置画笔
     */
    private void initPaintAndBitmap() {
        mPaint = new Paint();
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setDither(true);//设置防抖动
        mPaint.setStyle(Paint.Style.STROKE);//设置图形是空心的
        //设置白棋和黑棋图片
        mBlackChess = BitmapFactory.decodeResource(getResources(), R.drawable.black);
        mWhiteChess = BitmapFactory.decodeResource(getResources(), R.drawable.white);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChessBoard(canvas);//绘制棋盘（10*10网格）
        drawChesses(canvas);//绘制棋子
        checkGameResult();//游戏结束处理
    }

    private void checkGameResult() {
        isBlackWin = checkResult(mBlackList);
        isWhiteWin = checkResult(mWhiteList);

        if (isBlackWin || isWhiteWin) {
            isGameOver = true;
            if (null != mListener) {
                mListener.ResultListerer(isBlackWin ? 0 : 1);
            }
        }
    }

    private boolean checkResult(List<Point> pointList) {
        for (Point p : pointList) {
            int x = p.x;
            int y = p.y;
            boolean win = checkHorizontal(x, y, pointList);
            if (win) {
                return true;
            }
            win = checkVertical(x, y, pointList);
            if (win) {
                return true;
            }
            win = checkLeftSlant(x, y, pointList);
            if (win) {
                return true;
            }
            win = checkRightSlant(x, y, pointList);
            if (win) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查水平是否有五个相同棋子连接在一起
     *
     * @param x
     * @param y
     * @param pointList
     * @return
     */
    private boolean checkHorizontal(int x, int y, List<Point> pointList) {
        int count = 1;
        for (int i = 1; i < LINE_MAX_NUMS; i++) {
            if (pointList.contains(new Point(x - i, y)) || pointList.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == LINE_MAX_NUMS) {
            return true;
        }
        return false;
    }

    /**
     * 检查垂直方向是否有五个相同棋子连接在一起
     *
     * @param x
     * @param y
     * @param pointList
     * @return
     */
    private boolean checkVertical(int x, int y, List<Point> pointList) {
        int count = 1;
        for (int i = 1; i < LINE_MAX_NUMS; i++) {
            if (pointList.contains(new Point(x, y - i)) || pointList.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == LINE_MAX_NUMS) {
            return true;
        }
        return false;
    }

    /**
     * 检查左斜线是否有五个相同棋子连接在一起
     *
     * @param x
     * @param y
     * @param pointList
     * @return
     */
    private boolean checkLeftSlant(int x, int y, List<Point> pointList) {
        int count = 1;
        for (int i = 1; i < LINE_MAX_NUMS; i++) {
            if (pointList.contains(new Point(x - i, y + i)) || pointList.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == LINE_MAX_NUMS) {
            return true;
        }
        return false;
    }

    /**
     * 检查右斜线方向是否有五个相同棋子连接在一起
     *
     * @param x
     * @param y
     * @param pointList
     * @return
     */
    private boolean checkRightSlant(int x, int y, List<Point> pointList) {
        int count = 1;
        for (int i = 1; i < LINE_MAX_NUMS; i++) {
            if (pointList.contains(new Point(x - i, y - i)) || pointList.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == LINE_MAX_NUMS) {
            return true;
        }
        return false;
    }

    private void drawChesses(Canvas canvas) {
        //绘制白棋
        int wSize = mWhiteList.size();
        for (int i = 0; i < wSize; i++) {
            Point wp = mWhiteList.get(i);
            canvas.drawBitmap(mWhiteChess, (wp.x + (1 - mChessRatio) / 2) * mLineHeight, (wp.y + (1 - mChessRatio) / 2) * mLineHeight, null);
        }
        //绘制黑棋
        int bSize = mBlackList.size();
        for (int i = 0; i < bSize; i++) {
            Point bp = mBlackList.get(i);
            canvas.drawBitmap(mBlackChess, (bp.x + (1 - mChessRatio) / 2) * mLineHeight, (bp.y + (1 - mChessRatio) / 2) * mLineHeight, null);
        }
    }

    private void drawChessBoard(Canvas canvas) {
        for (int i = 0; i < MAX_LINES; i++) {
            int startX = (int) (mLineHeight / 2);
            int endX = (int) (mPanelWidth - mLineHeight / 2);
            int y = (int) ((0.5 + i) * mLineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);//绘制横线
            canvas.drawLine(y, startX, y, endX, mPaint);//绘制纵线
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//宽度大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//宽度测量模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);//高度大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//高度测量模式
        int width = Math.min(widthSize, heightSize);//设置界面宽度，棋盘是正方形
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);//设置大小
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINES;
        int chessSize = (int) (mLineHeight * mChessRatio);
        mBlackChess = Bitmap.createScaledBitmap(mBlackChess, chessSize, chessSize, false);//通过图片缩放设置棋子大小
        mWhiteChess = Bitmap.createScaledBitmap(mWhiteChess, chessSize, chessSize, false);
    }

    /**
     * 处理棋子的移动（下棋）
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {//如果游戏结束，则直接return
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);
            if (mWhiteList.contains(p) || mBlackList.contains(p)) {
                return false;
            }
            if (isWhiteFirst) {
                mWhiteList.add(p);
            } else {
                mBlackList.add(p);
            }
            invalidate();
            isWhiteFirst = !isWhiteFirst;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    /**
     * 重新开始（再来一局）
     */
    public void reStart() {
        mWhiteList.clear();
        mBlackList.clear();
        isGameOver = false;
        isWhiteWin = false;
        isBlackWin = false;
        invalidate();//重绘界面
    }

    /**
     * 保存界面数据（比如来电，李横竖屏切换等)
     *
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(GOBANG_INSTANCE, super.onSaveInstanceState());
        bundle.getBoolean(GAME_OVER, isGameOver);
        bundle.putParcelableArrayList(WHITE_DATA, mWhiteList);
        bundle.putParcelableArrayList(BLACK_DATA, mBlackList);
        return bundle;
    }

    /**
     * 取用保存的数据
     *
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            isGameOver = bundle.getBoolean(GOBANG_INSTANCE);
            mWhiteList = bundle.getParcelableArrayList(WHITE_DATA);
            mBlackList = bundle.getParcelableArrayList(BLACK_DATA);
            super.onRestoreInstanceState(bundle.getParcelable(GOBANG_INSTANCE));
        }
        super.onRestoreInstanceState(state);
    }
}
