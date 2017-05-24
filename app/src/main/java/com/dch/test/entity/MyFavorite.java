package com.dch.test.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * 作者：Dch on 2017/5/22 17:02
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@Entity
public class MyFavorite implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        MyFavorite favorite = new MyFavorite();
        favorite.id = dest.readLong();
        favorite.favoriteId = dest.readString();
        favorite.title = dest.readString();
        favorite.contentDiscription = dest.readString();
        favorite.collectTime = dest.readString();
        favorite.url = dest.readString();
        favorite.imgUrl = dest.readString();
    }

    public static final Creator<MyFavorite> CREATOR = new Parcelable.Creator<MyFavorite>(){

        @Override
        public MyFavorite createFromParcel(Parcel source) {
            MyFavorite favorite = new MyFavorite();
            favorite.id = source.readLong();
            favorite.favoriteId = source.readString();
            favorite.title = source.readString();
            favorite.contentDiscription = source.readString();
            favorite.collectTime = source.readString();
            favorite.url = source.readString();
            favorite.imgUrl = source.readString();
            return favorite;
        }

        @Override
        public MyFavorite[] newArray(int size) {
            return new MyFavorite[size];
        }
    };

}
