package com.dch.test.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

/**
 * 作者：Dch on 2017/4/27 12:09
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler instance = new CrashHandler();
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private Context context;

    private CrashHandler(){}

    public static CrashHandler getInstance(){
        return instance;
    }

    public void init(Context context){
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context;
    }


    /**
     *
     * @param t 出现未捕获异常的线程
     * @param e 未捕获的异常
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("出现异常");
        builder.setMessage(e.getMessage());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }
}
