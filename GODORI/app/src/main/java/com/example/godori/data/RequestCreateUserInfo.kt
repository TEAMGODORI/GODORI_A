package com.example.godori.data

data class RequestCreateUserInfo(
    val kakao_id: Int,
    val name: String,
    val nickname: String,
    val profile_img: String,
    val user_sport: String
)