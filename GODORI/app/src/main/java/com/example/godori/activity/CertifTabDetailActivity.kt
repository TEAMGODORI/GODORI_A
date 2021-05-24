package com.example.godori.activity

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.data.ResponseCertiDetail
import com.example.godori.data.ResponseGroupCreationData
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_certif_tab_detail.*
import kotlinx.android.synthetic.main.activity_group_info.*
import kotlinx.android.synthetic.main.activity_group_recruiting.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CertifTabDetailActivity : AppCompatActivity() {
    var data: ResponseCertiDetail.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certif_tab_detail)

        // 데이터 베이스에서 Person마다 정보 가져오기
        backBtn.setOnClickListener {
            onBackPressed()
        }
        //certiId 가져오기
        val certiImgId = intent.extras!!.getInt("certiImgId")
        Log.d("certiImgId", certiImgId.toString())
        loadData(certiImgId)

        // 좋아요 버튼
        btn_heart.setOnClickListener {
            // 1. 텍스트 컬러, 좋아요 수 변경
            if (btn_heart.isChecked) {
                text_heart_num.setTextColor(Color.parseColor("#61cbf1"))
                text_heart_num.text =
                    ((Integer.parseInt((text_heart_num.text).toString()) + 1).toString())
            } else {
                text_heart_num.setTextColor(Color.parseColor("#494949"))
                text_heart_num.text =
                    ((Integer.parseInt((text_heart_num.text).toString()) - 1).toString())
            }

            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d("CertiLike_KAKAOID", "토큰 정보 보기 실패")
                } else if (tokenInfo != null) {
                    Log.d(
                        "CertiLike_KAKAOID", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초"
                    )
                    // 2. 서버 연동
                    val call: Call<ResponseGroupCreationData> =
                        GroupRetrofitServiceImpl.service_ct_like.requestList(
                            kakaoId = tokenInfo.id,
                            certiId = certiImgId
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

                                    Log.v("좋아요", it.message.toString())

                                } ?: showError(response.errorBody())
                        }
                    })
                }
            }
        }
    }

    private fun loadData(certiImgId: Int) {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.d("CertiDetail_KAKAOID", "토큰 정보 보기 실패")
            } else if (tokenInfo != null) {
                Log.d(
                    "CertiDetail_KAKAOID", "토큰 정보 보기 성공" +
                            "\n회원번호: ${tokenInfo.id}" +
                            "\n만료시간: ${tokenInfo.expiresIn} 초"
                )
                //group_id로 그룹 정보 가져오기
                val call: Call<ResponseCertiDetail> =
                    GroupRetrofitServiceImpl.service_ct_detail.requestList(
                        kakaoId = tokenInfo.id,
                        certiId = certiImgId // 수정하기
                    )
                call.enqueue(object : Callback<ResponseCertiDetail> {
                    override fun onFailure(call: Call<ResponseCertiDetail>, t: Throwable) {
                        // 통신 실패 로직
                    }

                    @SuppressLint("SetTextI18n", "SimpleDateFormat")
                    override fun onResponse(
                        call: Call<ResponseCertiDetail>,
                        response: Response<ResponseCertiDetail>
                    ) {
                        response.takeIf { it.isSuccessful }
                            ?.body()
                            ?.let { it ->
                                // do something
                                data = response.body()?.data
                                Log.d("GroupRecruitingActivity", data.toString())

                                my_tv_userName.text = data?.user_name
//                        my_iv_profile.setImageURI(data?.user_image?.toUri())

                        // 좋아요 갯수
                        text_heart_num.text = data?.like_count.toString()

                        // 좋아요 눌린 상태면 채운 하트
                        when (data?.is_liked) {
                            true -> {
                                btn_heart.isChecked = true
                                text_heart_num.setTextColor(Color.parseColor("#61cbf1"))
                            }
                            else -> btn_heart.isChecked = false
                        }

                        val certiImgUrl: String = data!!.certi_images

                        if (certiImgUrl.length > 0) {
                            Glide.with(this@CertifTabDetailActivity)
                                .load(certiImgUrl)
                                .error(android.R.drawable.stat_notify_error)
                                .into(certiImg)

                        } else {
                            Glide.with(this@CertifTabDetailActivity)
                                .load(R.drawable.certif_un)
                                .error(android.R.drawable.stat_notify_error)
                                .into(certiImg)
                        }

                        val profileUrl: String = data!!.user_image

                        if(profileUrl.length > 0) {
                            Glide.with(this@CertifTabDetailActivity)
                                .load(certiImgUrl)
                                .error(android.R.drawable.stat_notify_error)
                                .into(my_iv_profile)

                        } else {
                            Glide.with(this@CertifTabDetailActivity)
                                .load(R.drawable.gr_img_profile_basic)
                                .error(android.R.drawable.stat_notify_error)
                                .into(my_iv_profile)
                        }

                        time_exercise.setText(data?.ex_time)

                        val sportList = it.data.sports.split(",")
                        val textArray = arrayOf<TextView>(
                            exercise1,
                            exercise2,
                            exercise3,
                            exercise4,
                            exercise5,
                            exercise6
                        )
                        for (i in sportList.indices) {
                            textArray[i].setVisibility(View.VISIBLE)
                            textArray[i].setText(sportList[i])
                        }

                        intensity.text = data?.ex_intensity
                        reviews.text = data?.ex_evalu
                        comment.text = data?.ex_comment
                    } ?: showError(response.errorBody())
                })
            }
        }
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Toast.makeText(this, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }
}