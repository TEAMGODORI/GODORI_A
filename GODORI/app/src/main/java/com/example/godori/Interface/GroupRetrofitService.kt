package com.example.godori.Interface

import com.example.godori.data.ResponseGroupRecruit
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

// 서버에서 호출할 메서드를 선언하는 인터페이스
// POST 방식으로 데이터를 주고 받을 때 넘기는 변수는 Field라고 해야한다.
interface GroupRetrofitService {
    @Headers("Content-Type:application/json")
    @GET("/group/list/{kakaoId}")
    fun requestList(
        @Path("kakaoId") kakaoId: Long
    ) : Call<ResponseGroupRecruit>
}
