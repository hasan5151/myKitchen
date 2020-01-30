package com.yaros.kitchen.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private String waiterToken;

    public AuthInterceptor(String apiKey) {
        waiterToken = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (waiterToken!=null) {
            HttpUrl url = chain.request().url()
                    .newBuilder()
                    .addQueryParameter("waiter_token", waiterToken)
                    .build();
            Request request = chain.request().newBuilder().url(url).build();
            return chain.proceed(request);
        }
        else
            return chain.proceed(chain.request());
    }
}