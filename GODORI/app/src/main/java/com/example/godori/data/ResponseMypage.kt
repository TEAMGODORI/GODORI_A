package com.example.godori.data

import java.net.URI

data class ResponseMypage(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
){
    data class Data(
        val profile: Profile,
        val certi_list: List<Certi>,
        val join: Join
    ){
        data class Profile(
            val name : String,
            val image: String
        )
        data class Join(
            val achive_rate: Int,
            val week_count: Int
        )
        data class Certi(
            val id: Int,
            val image: String
        )
    }
}