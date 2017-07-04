package com.dch.test.ui;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.util.StatusBarUtils;
import com.dch.test.widget.FiveChessesView;

import butterknife.BindView;

public class GameActivity extends BaseActivity {

    @BindView(R.id.chessesview)
    FiveChessesView chessesView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        chessesView.setIChessResultListener(new FiveChessesView.IChessResultListener() {
            @Override
            public void ResultListerer(int result) {
                if (0 == result){
                    //黑子胜
                    Snackbar.make(chessesView,"黑棋胜",Snackbar.LENGTH_LONG).setAction("再来一局", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chessesView.reStart();
                        }
                    }).show();
                } else {
                    Snackbar.make(chessesView,"白棋胜",Snackbar.LENGTH_LONG).setAction("再来一局", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chessesView.reStart();
                        }
                    }).show();
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_game;
    }
}
