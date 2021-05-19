package com.example.godori.data

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface CertiUploadImage {
    @Multipart
    @POST("/certi/image/{certiId}")
    fun postCertiUpload(
        @Path("certiId") certiId: Int,
        @Part images: MultipartBody.Part
    ): Call<ResponseGroupCreationData>
}