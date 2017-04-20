package com.dch.test.ui;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.util.StatusBarUtils;

import butterknife.BindView;

/**
 * 作者：Dch on 2017/4/17 17:02
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class DetailActivity extends BaseActivity {
    @BindView(R.id.iv_gank_detail)
    ImageView iv_gank_detail;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        StatusBarUtils.setImage(this);
        String url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).placeholder(R.drawable.guide4).into(iv_gank_detail);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_details;
    }
}
