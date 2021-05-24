package com.example.godori.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.data.ResponseGroupCreationData
import com.example.godori.data.ResponseGroupInfoAfter
import com.example.godori.fragment.GroupInfo1Flagment
import com.example.godori.fragment.GroupInfo2Fragment
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_group_info.*
import kotlinx.android.synthetic.main.activity_group_info_after.*
import kotlinx.android.synthetic.main.fragment_group_info1.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupInfoAfterActivity : AppCompatActivity() {
    var data: ResponseGroupInfoAfter? = null
    var groupData: ResponseGroupInfoAfter.Data.GroupDetail? = null
    var memberList: List<ResponseGroupInfoAfter.Data.GroupMember>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_info_after)

        val extras = intent.extras
        val groupId = extras!!.getInt("groupId")

        // 이전
        gr_btn_info_after_back.setOnClickListener {
            onBackPressed()
        }

        // 그룹, 그룹원 정보 - FrameLayout
        supportFragmentManager.beginTransaction()
            .replace(R.id.gr_frame_info, GroupInfo1Flagment())
            .commit()
        gr_btn_info_1.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.gr_frame_info, GroupInfo1Flagment())
                .commit()
        }
        gr_btn_info_2.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.gr_frame_info, GroupInfo2Fragment())
                .commit()
        }

        // 다른 그룹 구경가기
        gr_btn_otherGroup.setOnClickListener {
            val intent = Intent(this, GroupRecruitingActivity::class.java)
            startActivity(intent)
        }

        // 그룹 탈퇴하기
        gr_btn_exit.setOnClickListener {

            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d("LoginActivity", "토큰 정보 보기 실패")
                }
                else if (tokenInfo != null) {
                    Log.d(
                        "LoginActivity", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초"
                    )

                    // 1. 그룹 탈퇴 api 호출
                    val call: Call<ResponseGroupCreationData> =
                        GroupRetrofitServiceImpl.service_gr_exit.requestList(
                            kakaoId = tokenInfo.id // 수정하기
                        )
                    call.enqueue(object : Callback<ResponseGroupCreationData> {
                        override fun onFailure(call: Call<ResponseGroupCreationData>, t: Throwable) {
                            // 통신 실패 로직
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onResponse(
                            call: Call<ResponseGroupCreationData>,
                            response: Response<ResponseGroupCreationData>
                        ) {
                            response.takeIf { it.isSuccessful }
                                ?.body()
                                ?.let { it ->
                                    // do something

                                } ?: showError(response.errorBody())
                        }
                    })

                    // 2. 가입전 메인 화면으로
                    val intent = Intent(application, TabBarActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

        }

        //group_id로 그룹 정보 가져오기 GroupInfo1Fragment
        val call: Call<ResponseGroupInfoAfter> =
            GroupRetrofitServiceImpl.service_gr__info_after.requestList(
                groupId = groupId
            )
        call.enqueue(object : Callback<ResponseGroupInfoAfter> {
            override fun onFailure(call: Call<ResponseGroupInfoAfter>, t: Throwable) {
                // 통신 실패 로직
            }

            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onResponse(
                call: Call<ResponseGroupInfoAfter>,
                response: Response<ResponseGroupInfoAfter>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        // do something
                        data = response.body()
                        groupData = data!!.data.group_detail
                        Log.d("GroupRecruitingActivity", groupData.toString())
                        memberList = data!!.data.group_member
                        Log.d("GroupRecruitingActivity", memberList.toString())

                        gr_tv_info_after_title_exercise.setText(groupData!!.group_sport)
                        gr_tv_info_after_title_name.setText(groupData!!.group_name)
                        gr_tv_info_after_title_line.setText(groupData!!.intro_comment)
                        gr_tv_info_after_title_startdate.setText(groupData!!.created_at)
                        gr_tv_info_after_group_maker.setText(groupData!!.group_maker)

                        //group_image
                        val group_image = it.data.group_detail.group_image
                        @GlideModule
                        if (group_image != null) {
                            if (group_image.isNotEmpty()) {
                                Glide.with(this@GroupInfoAfterActivity)
                                    .load(group_image)
                                    .error(android.R.drawable.stat_notify_error)
                                    .into(gr_iv_info_img_after)

                            } else {
                                Glide.with(this@GroupInfoAfterActivity)
                                    .load(R.drawable.gr_img_info_title)
                                    .error(android.R.drawable.stat_notify_error)
                                    .into(gr_iv_info_img)
                            }
                        }
                    } ?: showError(response.errorBody())
            }
        })

    }
    // api 호출 에러 함수
    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
    }

    fun groupId():Int {
        val extras = intent.extras
        val groupId = extras!!.getInt("groupId")
        return groupId
    }
}