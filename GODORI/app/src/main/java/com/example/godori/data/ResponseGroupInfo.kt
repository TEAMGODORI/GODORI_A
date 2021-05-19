package com.example.godori.data

data class ResponseGroupInfo(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
){
    data class Data(
        val achive_rate: Int,
        val created_at: String,
        val ex_cycle: Int,
        val group_image: String,
        val ex_intensity: String,
        val group_maker: String,
        val group_name: String,
        val group_sport: String,
        val intro_comment: String,
        val id: Int,
        val recruit_num: Int,
        val recruited_num: Int
    )
}