package com.app.radiocity.network;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class ApiCall {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(new NetworkConnectionInterceptor(context))
//                .readTimeout(30, TimeUnit.SECONDS)
//                .connectTimeout(30, TimeUnit.SECONDS);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Log.e("OkHttp", message));
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(interceptor);
        clientBuilder.retryOnConnectionFailure(true);
        clientBuilder.connectTimeout(40, TimeUnit.SECONDS) // Adjust the timeout value as needed
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS);

//        .addInterceptor(new ConnectivityChangeReceiver(context))
//        client.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
//                .baseUrl("https://devmap.myradiocity.com/MAPWebService.asmx/")
                .baseUrl("https://maplite.myradiocity.com/MAPWebService.asmx/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(clientBuilder.build())
                .build();

        return retrofit;
    }

//    https://maplite.myradiocity.com/MAPWebService.asmx/
//    https://devmap.myradiocity.com/MAPWebService.asmx


}
