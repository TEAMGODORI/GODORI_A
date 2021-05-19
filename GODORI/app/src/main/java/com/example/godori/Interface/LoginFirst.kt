package com.example.godori.Interface

import com.example.godori.data.ResponseFirstLogin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LoginFirst {
    @GET("/user/{kakaoId}")
    fun requestList(
        @Path("kakaoId") kakaoId: Long
    ) : Call<ResponseFirstLogin>
}