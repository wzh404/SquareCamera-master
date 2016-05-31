package com.desmond.demo.service;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wangzunhui on 2016/5/25.
 */
public interface WebService {
    @GET("/drug/{code}")
    Observable<JsonObject> drug(@Path("code") String code);
}
