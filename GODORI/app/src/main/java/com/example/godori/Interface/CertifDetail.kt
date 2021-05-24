package com.example.godori.Interface

import com.example.godori.data.ResponseCertiDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CertifDetail {
    @GET("/certi/detail/{kakaoId}")
    fun requestList(
        @Path("kakaoId") kakaoId: Long,
        @Query("certiId") certiId: Int
    ) : Call<ResponseCertiDetail>
}