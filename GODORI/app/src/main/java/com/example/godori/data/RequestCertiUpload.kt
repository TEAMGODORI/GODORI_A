package com.example.godori.data

import retrofit2.http.Multipart
import java.io.File

data class RequestCertiUpload (
    val ex_time	: String,
    val ex_intensity : String,
    val ex_evalu : String,
    val ex_comment	: String,
    val certi_sport : String
)