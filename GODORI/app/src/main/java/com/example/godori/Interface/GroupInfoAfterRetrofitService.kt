package com.example.godori.Interface

import com.example.godori.data.ResponseGroupInfoAfter
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GroupInfoAfterRetrofitService {
    @Headers("Content-Type:application/json")
    @GET("/group/after/detail/{groupId}")
    fun requestList(
        @Path("groupId") groupId: Int
    ) : Call<ResponseGroupInfoAfter>
}