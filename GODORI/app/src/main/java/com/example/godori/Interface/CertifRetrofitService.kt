package com.example.godori.Interface

import com.example.godori.data.ResponseCertiTab
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

// 서버에서 호출할 메서드를 선언하는 인터페이스
interface CertifRetrofitService {
    @GET("/certi/{kakaoId}")
    fun requestList(
        @Path("kakaoId") kakaoId: Long,
        @Query("date") date: String
    ) : Call<ResponseCertiTab>
}