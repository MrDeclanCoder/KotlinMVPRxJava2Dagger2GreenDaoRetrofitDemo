package com.dch.test.util;

import android.widget.Toast;

import com.dch.test.base.BaseApplication;

/**
 * 作者：dch on 2017/4/10 16:48
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class ToastUtils {

    private static Toast toast;

    public static void showMessage(String content) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.application,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }


    public static void showLongTimeMessage(String content) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.application,
                    content,
                    Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
