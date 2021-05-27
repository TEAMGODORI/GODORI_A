package com.example.godori.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.data.RequestTasteSetting
import com.example.godori.data.ResponseGroupCreationData
import com.example.godori.data.ResponseGroupInfo
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_group_creation_complete.*
import kotlinx.android.synthetic.main.activity_group_info.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URL


class GroupInfoActivity : AppCompatActivity() {

    var data: ResponseGroupInfo? = null
    var dataD: ResponseGroupInfo.Data? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        // 슬라이드 효과
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            with(window) {
                requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
                // set an slide transition
                enterTransition = Slide(Gravity.END)
                exitTransition = Slide(Gravity.START)
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_info)

        var group_id: Int
        val extras = intent.extras
        group_id = extras!!.getInt("groupId")

        // 이전
        gr_btn_info_back.setOnClickListener {
            onBackPressed()
        }

        // 참여하기 버튼
        gr_btn_join_recruiting.setOnClickListener {

            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d("LoginActivity", "토큰 정보 보기 실패")
                }
                else if (tokenInfo != null) {
                    Log.d(
                        "GroupInfo_KAKAOID", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}"
                    )

                    val call: Call<ResponseGroupCreationData> =
                        GroupRetrofitServiceImpl.service_gr_join.requestList(
                            kakaoId = tokenInfo.id, // 수정하기
                            groupId = group_id
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
                                    // 이전 뷰 스택 다 지우고 TabBar 액티비티로 돌아가기
                                    val intent = Intent(application, TabBarActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                                    startActivity(intent)
                                } ?: showError(response.errorBody())
                        }
                    })
                }
            }

        }

        //group_id로 그룹 정보 가져오기
        val call: Call<ResponseGroupInfo> =
            GroupRetrofitServiceImpl.service_gr__info.requestList(
                groupId = group_id
            )
        call.enqueue(object : Callback<ResponseGroupInfo> {
            override fun onFailure(call: Call<ResponseGroupInfo>, t: Throwable) {
                // 통신 실패 로직
            }

            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onResponse(
                call: Call<ResponseGroupInfo>,
                response: Response<ResponseGroupInfo>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        // do something
                        data = response.body()
                        Log.d("GroupRecruitingActivity", data.toString())
                        dataD = data!!.data

                        //group_sport
                        gr_tv_info_title_exercise.setText(dataD!!.group_sport)
                        //group_name
                        gr_tv_info_title_name.setText(dataD!!.group_name)
                        //group_소개
                        gr_tv_info_title_line.setText(dataD!!.intro_comment)
                        //created_at
                        gr_tv_info_title_startdate.setText(dataD!!.created_at)
                        //group_maker
                        gr_create_name.setText(dataD!!.group_maker)

                        //recruited_num
                        gr_tv_info_member_recruit_num.setText(dataD!!.recruited_num.toString() + "/")
                        //recruit_num
                        gr_tv_info_member_total_num.setText(dataD!!.recruit_num.toString())
                        //ex_cycle
                        gr_tv_info_cycle_num.setText("주" + dataD!!.ex_cycle.toString() + "회")
                        //ex_intensity
                        gr_tv_info_level_num.setText(dataD!!.ex_intensity)
                        //achive_rate
                        gr_tv_info_percent_num.setText(dataD!!.achive_rate.toString() + "%")
                        //group_sport
                        gr_tv_info_exercise_num.setText(dataD!!.group_sport)
                        //group_image
                        val group_image = it.data.group_image
                        @GlideModule
                        if (group_image != null) {
                            if (group_image.isNotEmpty()) {
                                Glide.with(this@GroupInfoActivity)
                                    .load(group_image)
                                    .error(android.R.drawable.stat_notify_error)
                                    .into(gr_iv_info_img)

                            } else {
                                Glide.with(this@GroupInfoActivity)
                                    .load(R.drawable.gr_img_info_title)
                                    .error(android.R.drawable.stat_notify_error)
                                    .into(gr_iv_info_img)
                            }
                        }
                    }
            }
        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Toast.makeText(this, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }
}