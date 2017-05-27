package com.dch.test.manager

import java.net.URL

/**
 * 作者：MrCoder on 2017/5/26 17:26
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class RequestManager(val url: String) {
    public fun run() = URL(url).readText()
}