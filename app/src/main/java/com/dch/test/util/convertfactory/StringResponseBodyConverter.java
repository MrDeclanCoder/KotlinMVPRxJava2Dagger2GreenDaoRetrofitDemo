package com.dch.test.util.convertfactory;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 作者：Dch on 2017/4/12 18:11
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            return value.string();
        } finally {
            value.close();
        }
    }
}
