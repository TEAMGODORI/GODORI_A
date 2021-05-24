package com.example.godori.Interface

import com.example.godori.data.RequestGroupJoin
import com.example.godori.data.ResponseGroupCreationData
import retrofit2.Call
import retrofit2.http.*

interface GroupJoin {
    @Headers("Content-Type:application/json")
    @POST("/group/join/{kakaoId}")
    fun requestList(
        @Path("kakaoId") kakaoId: Long,
        @Query ("groupId") groupId: Int
    ) : Call<ResponseGroupCreationData>
}