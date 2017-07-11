package com.dch.test.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.dch.test.R;
import com.dch.test.util.ToastUtils;
import com.dch.test.widget.WaterRefreshView;
import com.dch.test.widget.WaterView;

public class TestViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        final WaterRefreshView waterRefreshView = (WaterRefreshView) findViewById(R.id.waterrefreshview);
        waterRefreshView.setOnRefreshListener(new WaterRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waterRefreshView.refreshSuccess();
                    }
                },2000);
            }
        });

        findViewById(R.id.tv_test_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage("我是测试TextView");
            }
        });
    }
}
