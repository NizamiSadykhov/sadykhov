package com.nizamisadykhov.gif_client.network

import com.nizamisadykhov.gif_client.model.PostDataResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("latest/{page}?json=true")
    fun getLatestPosts(@Path("page") page: Int): Observable<PostDataResponse>

    @GET("hot/{page}?json=true")
    fun getHotPosts(@Path("page") page: Int): Observable<PostDataResponse>

    @GET("top/{page}?json=true")
    fun getTopPosts(@Path("page") page: Int): Observable<PostDataResponse>
}