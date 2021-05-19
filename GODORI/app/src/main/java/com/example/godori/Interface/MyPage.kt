package com.example.godori.Interface

import com.example.godori.data.ResponseGroupAfterTab
import com.example.godori.data.ResponseMypage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MyPage {
    @GET("/mypage/{userName}")
    fun requestList(
        @Path("userName") userName: String
    ) : Call<ResponseMypage>
}