package com.example.godori.data

data class ResponseCertiTab(
    val `data`: List<Data>,
    val message: String,
    val status: Int,
    val success: Boolean
)
{
    data class Data(
        val id: Int,
        val image: String,
        val user_id: Int,
        val user_img: String,
        val user_name: String
    )
}