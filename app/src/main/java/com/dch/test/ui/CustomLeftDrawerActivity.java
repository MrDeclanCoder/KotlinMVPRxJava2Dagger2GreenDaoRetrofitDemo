package com.dch.test.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.remote.MusicService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dch on 2017/6/11.
 */

public class CustomLeftDrawerActivity extends BaseActivity implements View.OnClickListener{
    private final String TAG = "CustomLeftDrawActivity";

    @BindView(R.id.tv_music_play)
    TextView mTvPlay;
    @BindView(R.id.tv_music_stop)
    TextView mTvStop;
    @BindView(R.id.tv_music_select)
    TextView mTvSelect;

    private MusicService musicService;
    private MusicService.MyBinder myBinder;
    private boolean isBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"customleftdraweractivity onServiceConnected...");
            isBound = true;
            mTvPlay.setOnClickListener(CustomLeftDrawerActivity.this);
            mTvStop.setOnClickListener(CustomLeftDrawerActivity.this);
            myBinder = (MusicService.MyBinder) service;
            musicService = myBinder.getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"customleftdraweractivity onServiceConnected...");
            isBound = false;
        }
    };

    @Override
    protected void initData() {
        if (!isBound){
            Intent intent = new Intent(CustomLeftDrawerActivity.this, MusicService.class);
            bindService(intent,connection, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void initView() {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_custom_left_drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_music_play:
                processStartMusic();
                break;
            case R.id.tv_music_stop:
                processStopMusic();
                break;
        }
    }

    private void processStopMusic() {
        if (myBinder.isMusicPlaying()){
            musicService.stopPlayMusic();
            Log.d(TAG,"processStopMusic: success stop");
        } else {
            Log.d(TAG,"processStopMusic: have stop!!");
        }
        mTvSelect.setText(myBinder.isMusicPlaying()?"音乐播放中":"音乐停止");
    }

    private void processStartMusic() {
        if (myBinder.isMusicPlaying()){
            Log.d(TAG,"processStartMusic: have playing!!!");
        } else {
            musicService.startPlayMusic();
            Log.d(TAG,"processStartMusic: success start");
        }
        mTvSelect.setText(myBinder.isMusicPlaying()?"音乐播放中":"音乐停止");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound){
            unbindService(connection);
            isBound = false;
            Log.d(TAG,"ondestroy: cancel connect");
        }
    }
}
