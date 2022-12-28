package com.example.tugasakhir.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {
    private static final String baseURL = "https://api-dompetku.herokuapp.com";
    private static Retrofit retro;

    public static Retrofit connectRetro(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .build();


        if(retro == null) {
            retro = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retro;
    }

    public static APIRequestData create() {
        return connectRetro().create(APIRequestData.class);
    }
}
