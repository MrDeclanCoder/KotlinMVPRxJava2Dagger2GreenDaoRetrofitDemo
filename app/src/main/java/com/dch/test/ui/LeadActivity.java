package com.dch.test.ui;

import android.graphics.Path;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.mcxtzhang.pathanimlib.StoreHouseAnimView;
import com.mcxtzhang.pathanimlib.res.StoreHousePath;
import com.mcxtzhang.pathanimlib.utils.PathParserUtils;

import butterknife.BindView;

public class LeadActivity extends BaseActivity {

    @BindView(R.id.pathAnimView1)
    StoreHouseAnimView pathAnimView1;

    @Override
    protected void initData() {

        Path path = new Path();
        path.moveTo(0,0);
        path.addCircle(40,40,30, Path.Direction.CW);
        pathAnimView1.setSourcePath(PathParserUtils.getPathFromArrayFloatList(StoreHousePath.getPath("pathview")));
        pathAnimView1.startAnim();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_lead;
    }
}
