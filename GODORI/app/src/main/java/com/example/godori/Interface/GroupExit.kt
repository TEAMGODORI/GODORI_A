package com.example.godori.Interface

import com.example.godori.data.ResponseGroupAfterTab
import com.example.godori.data.ResponseGroupCreationData
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroupExit {
    @Headers("Content-Type:application/json")
    @PUT("/group/{kakaoId}")
    fun requestList(
        @Path("kakaoId") kakaoId: Long
    ) : Call<ResponseGroupCreationData>
}