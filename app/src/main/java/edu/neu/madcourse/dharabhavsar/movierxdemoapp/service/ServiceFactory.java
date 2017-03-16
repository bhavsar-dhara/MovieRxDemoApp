package edu.neu.madcourse.dharabhavsar.movierxdemoapp.service;

import retrofit2.Retrofit;

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
        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();
        T service = restAdapter.create(tClass);

        return service;
    }
}
