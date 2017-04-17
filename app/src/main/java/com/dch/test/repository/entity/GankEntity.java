package com.dch.test.repository.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Dch on 2017/4/17 14:07
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class GankEntity {
    public String error;
    public List<Data> results = new ArrayList<>();

    public static class Data{
        public String _id;
        public String createdAt;
        public String desc;
        public String[] images;
        public String publishedAt;
        public String source;
        public String type;
        public String url;
        public String used;
        public String who;
    }
}
