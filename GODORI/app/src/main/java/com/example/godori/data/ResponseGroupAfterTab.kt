package com.example.godori.data

data class ResponseGroupAfterTab(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class Data(
        val group_cycle: Int,
        val group_id: Int,
        val group_name: String,
        val left_count: Int,
        val not_today_member: List<NotTodayMember>,
        val today_member: List<TodayMember>
    ) {
        data class NotTodayMember(
            val user_id: Int,
            val user_img: String,
            val user_name: String,
            val week_count: Int
        )

        data class TodayMember(
            val user_id: Int,
            val user_img: String,
            val user_name: String,
            val week_count: Int
        )
    }
}