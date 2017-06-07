package com.dch.test.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dch.test.R;

import java.util.ArrayList;

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
    private ArrayList<Point> mWhiteList = new ArrayList<>();//白棋子数据
    private boolean isWhiteFirst = false;//是否先出白棋
    private boolean isBlackWin = false;//黑棋子赢
    private boolean isWhiteWin = false;//白棋子赢
    private boolean isGameOver = false;//游戏是否结束
    private final static int LINE_MAX_NUMS = 6;//同色五子成线表示结束
    //以下几个常量是为了保存界面数据
    private final static String GOBANG_INSTANCE = "gobang_instance";
    private final static String GAME_OVER = "game_over";
    private final static String WHITE_DATA = "white_data";
    private final static String BLACK_DATA = "black_data";

    public FiveChessesView(Context context) {
        this(context, null);
    }

    public FiveChessesView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FiveChessesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0x44ff0000);
        initPaintAndBitmap();
    }

    private void initPaintAndBitmap() {
        mPaint = new Paint();
        mPaint.setColor(0x88000000);
        mPaint.setDither(true);//设置防抖动
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);//设置圆心是空心的

        //设置棋子图片
        mWhiteChess = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        mBlackChess = BitmapFactory.decodeResource(getResources(), R.drawable.black);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = Math.min(widthSize, heightSize);//设置宽度为正方形
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINES;
        int chessSize = (int) (mLineHeight * mChessRatio);
        mBlackChess = Bitmap.createScaledBitmap(mBlackChess, chessSize, chessSize, false);//通过图片缩放设置棋子大小
        mWhiteChess = Bitmap.createScaledBitmap(mWhiteChess, chessSize, chessSize, false);//通过图片缩放设置棋子大小
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChessBoard(canvas);
        drawChesses(canvas);
        checkGameResult();//检查游戏每一步结果
    }

    private void checkGameResult() {
        isBlackWin = cheResult(mBlackList);
        isWhiteWin = cheResult(mWhiteList);

        if (isBlackWin || isWhiteWin) {
            isGameOver = true;
            if (null != listener) {
                listener.onResultListener(isBlackWin ? 0 : 1);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {//游戏结束
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                Point validPoint = getValidPoint(x, y);
                if (mWhiteList.contains(validPoint) || mBlackList.contains(validPoint)) {
                    //已存在
                    return false;
                }
                if (isWhiteFirst) {
                    mWhiteList.add(validPoint);
                } else {
                    mBlackList.add(validPoint);
                }
                invalidate();
                isWhiteFirst = !isWhiteFirst;
                break;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    private boolean cheResult(ArrayList<Point> list) {
        for (Point p : list) {
            int x = p.x;
            int y = p.y;
            boolean win = checkHorizontal(x, y, list);
            if (win) {
                return true;
            }
            win = checkVertical(x, y, list);
            if (win) {
                return true;
            }
            win = checkLeftSlant(x, y, list);
            if (win) {
                return true;
            }
            win = checkRightSlant(x, y, list);
            if (win) {
                return true;
            }
        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, ArrayList<Point> pointList) {
        int count = 1;
        for (int i = 0; i < LINE_MAX_NUMS; i++) {
            if (pointList.contains(new Point(x - i, y)) || pointList.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        return count == LINE_MAX_NUMS;
    }

    private boolean checkVertical(int x, int y, ArrayList<Point> pointList) {
        int count = 1;
        for (int i = 0; i < LINE_MAX_NUMS; i++) {
            if (pointList.contains(new Point(x, y - i)) || pointList.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        return count == LINE_MAX_NUMS;
    }

    private boolean checkLeftSlant(int x, int y, ArrayList<Point> pointList) {
        int count = 1;
        for (int i = 0; i < LINE_MAX_NUMS; i++) {
            if (pointList.contains(new Point(x - i, y - i)) || pointList.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        return count == LINE_MAX_NUMS;
    }

    private boolean checkRightSlant(int x, int y, ArrayList<Point> pointList) {
        int count = 1;
        for (int i = 0; i < LINE_MAX_NUMS; i++) {
            if (pointList.contains(new Point(x - i, y + i)) || pointList.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        return count == LINE_MAX_NUMS;
    }

    private void drawChesses(Canvas canvas) {
        //绘制黑棋
        int blackSize = mBlackList.size();
        for (int i = 0; i < blackSize; i++) {
            Point bp = mBlackList.get(i);
            canvas.drawBitmap(mBlackChess, (bp.x + (1 - mChessRatio) / 2) * mLineHeight, (bp.y + (1 - mChessRatio) / 2) * mLineHeight, null);
        }

        //绘制白棋
        int whiteSize = mWhiteList.size();
        for (int i = 0; i < whiteSize; i++) {
            Point wp = mWhiteList.get(i);
            canvas.drawBitmap(mWhiteChess, (wp.x + (1 - mChessRatio) / 2) * mLineHeight, (wp.y + (1 - mChessRatio) / 2) * mLineHeight, null);
        }
    }

    private void drawChessBoard(Canvas canvas) {
        for (int i = 0; i < MAX_LINES; i++) {
            int startX = (int) (mLineHeight / 2);
            int endX = mPanelWidth - startX;
            int y = (int) ((0.5 + i) * mLineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);//绘制横线
            canvas.drawLine(y, startX, y, endX, mPaint);//绘制纵线
        }
    }

    public void restart(){
        mBlackList.clear();
        mWhiteList.clear();
        isGameOver = false;
        isWhiteWin = false;
        isBlackWin = false;
        invalidate();
    }

    public IChessResultListener listener;

    public void setOnResultListener(IChessResultListener listener) {
        this.listener = listener;
    }

    public interface IChessResultListener {
        void onResultListener(int result);
    }
}
