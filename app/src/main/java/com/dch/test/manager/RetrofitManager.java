package com.dch.test.manager;

import com.dch.test.repository.remote.CSDNApi;

/**
 * 作者：Dch on 2017/4/11 11:37
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class RetrofitManager {
    private CSDNApi csdnApi;

    public RetrofitManager(CSDNApi csdnApi){
        this.csdnApi = csdnApi;
    }
}
