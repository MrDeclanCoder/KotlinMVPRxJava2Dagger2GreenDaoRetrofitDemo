package com.dch.test.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.dch.test.R;
import com.dch.test.widget.WaterRefreshView;
import com.dch.test.widget.WaterView;

public class TestViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        WaterRefreshView waterRefreshView = (WaterRefreshView) findViewById(R.id.waterrefreshview);

    }
}
