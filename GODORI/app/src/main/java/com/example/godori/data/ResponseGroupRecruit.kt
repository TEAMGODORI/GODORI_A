package com.example.godori.data

data class ResponseGroupRecruit(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
){
    data class Data(
        val group_list: List<Group>,
        val user: User
    ){
        data class Group(
            val created_at: String,
            val ex_cycle: Int,
            val ex_intensity: String,
            val group_name: String,
            val group_sport: String,
            val id: Int,
            val intro_comment: String,
            val parse_date: String,
            val recruit_num: Int,
            val recruited_num: Int
        )
        data class User(
            val ex_cycle: Int,
            val ex_intensity: String,
            val id: Int,
            val name: String,
            val sports: String
        )
    }
}