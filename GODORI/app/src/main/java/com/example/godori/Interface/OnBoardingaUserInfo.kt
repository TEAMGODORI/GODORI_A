package com.example.godori.Interface

import com.example.godori.data.RequestCreateUserInfo
import com.example.godori.data.ResponseCreateUserInfo
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface OnBoardingaUserInfo {
    @POST("/user")
    fun requestList(
        @Body body: RequestCreateUserInfo
    ):Call<ResponseCreateUserInfo>
}