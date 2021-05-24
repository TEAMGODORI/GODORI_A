package com.example.godori.data

import java.io.File

data class RequestGroupCreationData(
    val group_name: String,
    val recruit_num: Int,
    val is_public: Boolean,
    val intro_comment: String,
    val ex_cycle: Int,
    val ex_intensity: String,
    val group_sport: String,
    val kakao_id: Long
)
