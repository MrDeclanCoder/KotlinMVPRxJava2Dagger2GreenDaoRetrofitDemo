package com.dch.test.util;

import android.app.Activity;
import android.content.Intent;

/**
 * 作者：Dch on 2017/4/17 11:30
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class ActivityUtils {

    public static void startActivity(Activity activity, Class clazz){
        activity.startActivity(new Intent(activity,clazz));
    }
}
