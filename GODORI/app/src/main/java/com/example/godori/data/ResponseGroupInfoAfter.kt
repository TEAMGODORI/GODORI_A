package com.example.godori.data

data class ResponseGroupInfoAfter(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
){
    data class Data(
        val group_detail: GroupDetail,
        val group_member: List<GroupMember>
    ){
        data class GroupDetail(
            val achive_rate: Int,
            val created_at: String,
            val ex_cycle: Int,
            val ex_intensity: String,
            val group_maker: String,
            val group_name: String,
            val group_sport: String,
            val group_image: String,
            val id: Int,
            val intro_comment: String,
            val recruit_num: Int,
            val recruited_num: Int
        )

        data class GroupMember(
            val id: Int,
            val name: String,
            val profile_img: String
        )
    }
}