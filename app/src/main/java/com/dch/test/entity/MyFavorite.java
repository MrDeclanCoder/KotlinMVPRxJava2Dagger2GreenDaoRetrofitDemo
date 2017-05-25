package com.dch.test.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

/**
 * 作者：Dch on 2017/5/22 17:02
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@Entity
public class MyFavorite implements Serializable {

    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String favoriteId;
    private String title;
    private String contentDiscription;
    private String collectTime;
    private String url;
    private String imgUrl;

    @Generated(hash = 2012303583)
    public MyFavorite(Long id, String favoriteId, String title,
                      String contentDiscription, String collectTime, String url,
                      String imgUrl) {
        this.id = id;
        this.favoriteId = favoriteId;
        this.title = title;
        this.contentDiscription = contentDiscription;
        this.collectTime = collectTime;
        this.url = url;
        this.imgUrl = imgUrl;
    }

    @Generated(hash = 1538796775)
    public MyFavorite() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFavoriteId() {
        return this.favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentDiscription() {
        return this.contentDiscription;
    }

    public void setContentDiscription(String contentDiscription) {
        this.contentDiscription = contentDiscription;
    }

    public String getCollectTime() {
        return this.collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
