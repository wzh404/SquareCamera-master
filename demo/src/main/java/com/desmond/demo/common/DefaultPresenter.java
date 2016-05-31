package com.desmond.demo.common;

import com.desmond.demo.DrugApplication;
import com.desmond.demo.service.WebService;
import com.desmond.demo.common.action.ErrorAction1;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.common.action.ResultAction1;
import com.desmond.demo.common.rxbus.RxBus;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wangzunhui on 2016/5/31.
 */
public class DefaultPresenter implements Presenter {
    private WebService webService;
    private Map<String, Observable> map = new HashMap<String, Observable>();

    public DefaultPresenter(){
        webService = DrugApplication.getRetrofit().create(WebService.class);
    }

    public WebService getWebService(){
        return this.webService;
    }

    @Override
    public void destroy() {
        for (Map.Entry<String, Observable> entry : map.entrySet()) {
            RxBus.get().unregister(entry.getKey(), entry.getValue());
        }
    }

    public void register(String tag, Action1 action1) {
        Observable<Result> observable = RxBus.get().register(tag, Result.class);
        observable.subscribeOn(AndroidSchedulers.mainThread())
                  .subscribe(action1);

        map.put(tag, observable);
    }

    public void call(Observable<JsonObject> observable, String tag) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultAction1(tag), new ErrorAction1(tag));
    }
}
