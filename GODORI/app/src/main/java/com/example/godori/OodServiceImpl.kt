package com.example.godori

import com.example.godori.Interface.GroupCreation
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OodServiceImpl {
    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()
    // 서버 주소
    private const val BASE_URL = "http://15.164.186.213:3000/"

    // SingleTon
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service_gr_creation: GroupCreation = retrofit.create(GroupCreation::class.java)
}