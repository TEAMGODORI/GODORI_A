package com.example.godori.Interface

import com.example.godori.data.ResponseGroupCreationData
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CertiLike {
    @PUT("/certi/like/{userName}")
    fun requestList(
        @Path("userName") userName: String,
        @Query("certiId") certiId:Int
    ) : Call<ResponseGroupCreationData>
}