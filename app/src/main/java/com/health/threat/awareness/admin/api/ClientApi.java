package com.health.threat.awareness.admin.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApi {
    private static Retrofit retrofit=null;
    public static  Retrofit getRetrofit(String url){
        if(retrofit==null){

            return retrofit=new Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
