package com.example.godori.Interface

import com.example.godori.data.ResponseGroupAfterTab
import com.example.godori.data.ResponseGroupRecruit
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GroupAfter {
    @Headers("Content-Type:application/json")
    @GET("/group/member/{kakaoId}")
    fun requestList(
        @Path("kakaoId") kakaoId: Long
    ) : Call<ResponseGroupAfterTab>
}