package com.example.godori.Interface

import com.example.godori.data.RequestGroupCreationData
import com.example.godori.data.ResponseGroupCreationData
import com.example.godori.data.ResponseGroupSearch
import retrofit2.Call
import retrofit2.http.*

interface GroupSearch {
    @GET("/group")
    fun groupSearch(
    ): Call<ResponseGroupSearch>
}