package edu.neu.madcourse.dharabhavsar.movierxdemoapp.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dhara on 3/15/2017.
 */

public class ServiceFactory {

    /**
     * Creates a retrofit service from an arbitrary class (tClass)
     * @param tClass Java interface of the retrofit service
     * @param baseUrl REST endpoint url
     * @return retrofit service with defined baseUrl
     */
    public static <T> T createRetrofitService(final Class<T> tClass, final String baseUrl) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(tClass);
    }
}
