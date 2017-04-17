package com.dch.test.repository.remote;

import android.support.annotation.NonNull;

import com.dch.test.manager.RetrofitManager;
import com.dch.test.repository.ArticalDataSource;
import com.dch.test.repository.entity.GankEntity;
import com.dch.test.repository.remote.apistores.GankApiService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
        RetrofitManager.getInstance().createCsdnApiService()
                .getArticalList()
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                        return parseData(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<String> s) {
                        callback.onArticalLoaded((ArrayList<String>) s);
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

    @Override
    public void getMeiziData(@NonNull GankCallback callback) {
        RetrofitManager.getInstance()
                .createGankApiService()
                .getDailyMeiziData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(GankEntity gankEntity) {
                        callback.onGankdataLoaded(gankEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onDataNotAvailable(t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getAndroidData(@NonNull GankCallback callback) {
        RetrofitManager.getInstance()
                .createGankApiService()
                .getDailyAndroidData("Android",20,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(GankEntity entity) {
                        for (GankEntity.Data data : entity.results){
                            System.out.println(data._id);
                            System.out.println(data.createdAt);
                            System.out.println(data.desc);
                        }
                        callback.onGankdataLoaded(entity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("getAndroidData--"+t.getMessage());
                        callback.onDataNotAvailable(t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private List<String> parseData(String html) {
        //jsoup解析数据
        Document document = Jsoup.parse(html);
        String title = document.title();
        ArrayList<String> strings = new ArrayList<>();
        strings.add(title);

        Elements ul = document.getElementsByTag("ul");
        for (Element element : ul) {
            if (ul.hasClass("panel_body itemlist")) {
                Elements a = element.getElementsByTag("a");
                for (Element aa : a) {
                    if (aa.ownText().length() > 20)
                        strings.add(aa.ownText());
                }
            }
        }
        return strings;
    }
}
