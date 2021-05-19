package com.example.godori.Interface

import com.example.godori.data.RequestGroupJoin
import com.example.godori.data.ResponseGroupCreationData
import retrofit2.Call
import retrofit2.http.*

interface GroupJoin {
    @Headers("Content-Type:application/json")
    @POST("/group/join/{userName}")
    fun requestList(
        @Path("userName") userName: String,
        @Query ("groupId") groupId: Int
    ) : Call<ResponseGroupCreationData>
}