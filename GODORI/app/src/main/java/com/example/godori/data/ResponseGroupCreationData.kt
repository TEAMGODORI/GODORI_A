package com.example.godori.data

data class ResponseGroupCreationData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: String
)