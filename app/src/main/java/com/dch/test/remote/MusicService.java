package com.dch.test.remote;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 作者：MrCoder on 2017/7/7 15:52
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class MusicService extends Service {
    private final String TAG = "MusicService";

    private boolean isMusicPlay = false;
    private MyBinder myBinder = new MyBinder();

    public void startPlayMusic(){
        isMusicPlay = true;
        Log.d(TAG,"startPlayMusic");
    }

    public void stopPlayMusic(){
        isMusicPlay = false;
        Log.d(TAG,"stopPlayMusic");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends  Binder{

        public MusicService getMusicService(){
            return MusicService.this;
        }

        public boolean isMusicPlaying(){
            return isMusicPlay;
        }

    }
}
