package com.example.dalaptrinhapp.RetrofitManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    static Retrofit retrofitInstance;
    public static Retrofit getInstance() {
            Retrofit retrofitInstance = new Retrofit.Builder()
                    .baseUrl("https://bookolshop.000webhostapp.com/android/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return retrofitInstance;
    }
}
