package com.example.godori.Interface

import com.example.godori.data.RequestGroupCreationData
import com.example.godori.data.ResponseGroupCreationData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupCreation {
    @POST("/group")
    fun postGroupCreation(
        @Body body: RequestGroupCreationData
    ): Call<ResponseGroupCreationData>
}