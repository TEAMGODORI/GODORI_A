package com.example.godori.Interface

import com.example.godori.data.ResponseGroupAfterTab
import com.example.godori.data.ResponseMypage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MyPage {
    @GET("/mypage/{kakaoId}")
    fun requestList(
        @Path("kakaoId") kakaoId: Long
    ) : Call<ResponseMypage>
}