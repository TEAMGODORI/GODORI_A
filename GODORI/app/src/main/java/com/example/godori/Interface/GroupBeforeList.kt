package com.example.godori.Interface

import com.example.godori.data.ResponseGroupBeforeTab
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface GroupBeforeList {
    @Headers("Content-Type:application/json")
    @GET("/group/before/list")
    fun requestList(
    ) : Call<ResponseGroupBeforeTab>
}