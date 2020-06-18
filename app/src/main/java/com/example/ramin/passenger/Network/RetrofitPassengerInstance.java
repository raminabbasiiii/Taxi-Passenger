package com.example.ramin.passenger.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitPassengerInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://ramin-abbasi.ir/taxiApi/passenger/";

    private RetrofitPassengerInstance() {
    }

    public static Retrofit getRetrofitPassenger() {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
