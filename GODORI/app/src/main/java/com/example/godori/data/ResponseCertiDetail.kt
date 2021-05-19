package com.example.godori.data

data class ResponseCertiDetail(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
){
    data class Data(
        val certi_images: String,
        val ex_comment: String,
        val ex_evalu: String,
        val ex_intensity: String,
        val ex_time: String,
        val sports: String,
        val user_id: Int,
        val user_image: String,
        val user_name: String,
        val like_count: Int,
        val is_liked: Boolean
    )
}