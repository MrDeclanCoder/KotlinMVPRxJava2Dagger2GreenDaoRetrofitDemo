package com.dch.test.util;

import android.widget.Toast;

import com.dch.test.base.BaseApplication;

/**
 * 作者：dch on 2017/4/10 16:48
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class ToastUtils {

    public static void showMessage(String msg){
        Toast.makeText(BaseApplication.application, msg, Toast.LENGTH_SHORT).show();
    }


    public static void showLongTimeMessage(String msg){
        Toast.makeText(BaseApplication.application, msg, Toast.LENGTH_LONG).show();
    }
}
