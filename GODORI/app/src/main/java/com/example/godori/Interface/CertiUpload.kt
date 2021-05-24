package com.example.godori.Interface

import com.example.godori.data.RequestCertiUpload
import com.example.godori.data.ResponseCertiUpload
import retrofit2.Call
import retrofit2.http.*

interface CertiUpload {
    @POST("/certi/{kakaoId}")
    fun postCertiUpload(
        @Path("kakaoId") kakaoId: Long,
        @Body body: RequestCertiUpload
    ): Call<ResponseCertiUpload>
}