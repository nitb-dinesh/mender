package com.app.handyman.mender.common.rest;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dinesh Prajapati on 13/4/18.
 */
public class AppConfig {

    public static final String BASE_URL = "http://optimaiot.com/";

    private static Retrofit retrofit = null;
    private static LoadInterface loadInterface = null;

    private static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static LoadInterface getLoadInterface() {
        if (loadInterface == null) {
            loadInterface = AppConfig.getClient().create(LoadInterface.class);
        }
        return loadInterface;
    }
}
