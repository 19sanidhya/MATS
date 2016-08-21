package com.media.sanidhya.mats;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sanidhya on 6/7/16.
 */
public class MovieReviewClient {

    private static MovieAPIService service;

    public static MovieAPIService getService() {
        if (service == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(45, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request r = chain.request();
                            r = r.newBuilder().addHeader("device_type", "android").build();
                            return chain.proceed(r);

                        }
                    })
                    .build();
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/").
                    addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .client(client).build();
            service = retrofit.create(MovieAPIService.class);
        }
        return service;

    }
}

