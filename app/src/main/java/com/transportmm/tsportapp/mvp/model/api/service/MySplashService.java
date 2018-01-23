package com.transportmm.tsportapp.mvp.model.api.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Administrator on 2017/9/22.
 */

public interface MySplashService {

    @Headers("Content-Type:text/plain;charset=UTF-8")
    @GET("wmaps/xml/china.xml")
    Observable<String> getCities();
}
