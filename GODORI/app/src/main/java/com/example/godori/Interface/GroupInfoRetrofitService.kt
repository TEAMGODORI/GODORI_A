package com.example.godori.Interface

import com.example.godori.data.ResponseGroupInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GroupInfoRetrofitService {
    @Headers("Content-Type:application/json")
    @GET("/group/detail/{groupId}")
    fun requestList(
        @Path("groupId") groupId: Int
    ) : Call<ResponseGroupInfo>
}