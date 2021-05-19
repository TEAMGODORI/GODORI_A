package com.example.godori.Interface

import com.example.godori.data.RequestTasteSetting
import com.example.godori.data.ResponseGroupCreationData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface TasteSetting {
    @PUT("/user/{userName}")
    fun taste(
        @Path ("userName") userName: String,
        @Body body: RequestTasteSetting
    ): Call<ResponseGroupCreationData>
}