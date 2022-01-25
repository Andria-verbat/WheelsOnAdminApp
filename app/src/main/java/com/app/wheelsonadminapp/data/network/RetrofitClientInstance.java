package com.app.wheelsonadminapp.data.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.wheelsonadminapp.util.AppConstants.BASE_URL;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static ApiService apiService;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService(){
        if(apiService == null){
            apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        }
        return apiService;
    }
}
