package com.dch.test.repository.remote;

import android.support.annotation.NonNull;

import com.dch.test.manager.RetrofitManager;
import com.dch.test.repository.ArticalDataSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：Dch on 2017/4/10 18:20
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class ArticalRemoteDataSource implements ArticalDataSource {

    private static ArticalRemoteDataSource INSTANCE;

    public static ArticalRemoteDataSource getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ArticalRemoteDataSource();
        }
        return INSTANCE;
    }

    private ArticalRemoteDataSource() {
    }

    @Override
    public void getArticalsData(@NonNull LoadArticalCallback callback) {
        RetrofitManager.getInstance().createApiService()
                .getArticalList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                        //jsoup解析数据
                        Document document = Jsoup.parse(s);
                        String title = document.title();
                        ArrayList<String> strings = new ArrayList<>();
                        strings.add(title);

                        Elements ul = document.getElementsByTag("ul");
                        for (Element element : ul) {
                            if (ul.hasClass("panel_body itemlist")) {
                                Elements a = element.getElementsByTag("a");
                                for (Element aa : a) {
                                    if (aa.ownText().length() > 3)
                                        strings.add(aa.ownText());
                                }
                            }
                        }
                        callback.onArticalLoaded(strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
