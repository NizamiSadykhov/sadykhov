package com.nizamisadykhov.gif_client.network

import com.nizamisadykhov.gif_client.data.Constants.BASE_API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfigurator {
    fun getApiClient(): ApiService {
        return getRetrofit().create(ApiService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        val rxAdapter = RxJava3CallAdapterFactory.create()
        return Retrofit.Builder().baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .addCallAdapterFactory(rxAdapter).build()
    }
}