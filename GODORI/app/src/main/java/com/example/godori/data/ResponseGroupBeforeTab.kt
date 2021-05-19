package com.example.godori.data

data class ResponseGroupBeforeTab(
    val `data`: List<Data>,
    val message: String,
    val status: Int,
    val success: Boolean
){
    data class Data(
        val created_at: String,
        val ex_cycle: Int,
        val ex_intensity: String,
        val group_image: String,
        val group_name: String,
        val group_sport: String,
        val id: Int,
        val intro_comment: String,
        val recruit_num: Int
    )
}