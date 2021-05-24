package com.example.godori.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.data.RequestGroupCreationData
import com.example.godori.data.ResponseCertiTab
import com.example.godori.data.ResponseGroupCreationData
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_certif_tab_detail.*
import kotlinx.android.synthetic.main.activity_group_creation4.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Boolean

class GroupCreation4Activity : AppCompatActivity() {

    // 데이터 목록
    var group_name: String = ""
    var recruit_num: Int = 0
    var is_public: kotlin.Boolean = Boolean.TRUE
    var intro_comment: String = ""
    var ex_cycle: Int = 0
    var ex_intensity: String = ""
    var group_sport: String = ""
    var kakao_id: Long = 0

    var data: ResponseGroupCreationData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation4)

        // 이전
        gr_btn_creation4_back.setOnClickListener {
            onBackPressed()
        }

        // 운동 주기
        gr_rg_creation4_cycle.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.gr_rb_creation4_cycle1 -> ex_cycle = 1
                R.id.gr_rb_creation4_cycle2 -> ex_cycle = 2
                R.id.gr_rb_creation4_cycle3 -> ex_cycle = 3
                R.id.gr_rb_creation4_cycle4 -> ex_cycle = 4
                R.id.gr_rb_creation4_cycle5 -> ex_cycle = 5
                R.id.gr_rb_creation4_cycle6 -> ex_cycle = 6
                R.id.gr_rb_creation4_cycle7 -> ex_cycle = 7
            }
        }

        // 운동 강도
        groupCreation_intensity.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.groupCreation_intensity1 -> ex_intensity = "상"
                R.id.groupCreation_intensity2 -> ex_intensity = "중"
                R.id.groupCreation_intensity3 -> ex_intensity = "하"
            }
        }

        // 선호하는 운동
        gr_cb_creation4_exercise1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                gr_btn_creation4_next.isEnabled = true
                group_sport = "헬스"
            }
        }
        gr_cb_creation4_exercise2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                gr_btn_creation4_next.isEnabled = true
                group_sport = "러닝"
            }
        }
        gr_cb_creation4_exercise3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                gr_btn_creation4_next.isEnabled = true
                group_sport = "요가/필테"
            }
        }
        gr_cb_creation4_exercise4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                gr_btn_creation4_next.isEnabled = true
                group_sport = "등산"
            }
        }
        gr_cb_creation4_exercise5.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                gr_btn_creation4_next.isEnabled = true
                group_sport = "자전거"
            }
        }
        gr_cb_creation4_exercise6.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                gr_btn_creation4_next.isEnabled = true
                group_sport = "수영"
            }
        }

        // 다음 버튼
        gr_btn_creation4_next.setOnClickListener {

            // 1. 데이터 전달 받기
            val secondIntent = getIntent()
            group_name = secondIntent.getStringExtra("group_name").toString()
            recruit_num = secondIntent.getIntExtra("recruit_num", 0)
            is_public = secondIntent.getBooleanExtra("is_public", false)
            intro_comment = secondIntent.getStringExtra("intro_comment").toString()

            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d("GroupCreation4_KAKAOID", "토큰 정보 보기 실패")
                } else if (tokenInfo != null) {
                    Log.d(
                        "GroupCreation4_KAKAOID", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초"
                    )

                    // 2. 그룹 생성하기 POST
                    val call: Call<ResponseGroupCreationData> =
                        GroupRetrofitServiceImpl.service_gr_creation.postGroupCreation(
                            RequestGroupCreationData(
                                group_name = group_name,
                                recruit_num = recruit_num,
                                is_public = is_public,
                                intro_comment = intro_comment,
                                ex_cycle = ex_cycle,
                                ex_intensity = ex_intensity,
                                group_sport = group_sport,
                                kakao_id = tokenInfo.id
                            )
                        )
                    call.enqueue(object : Callback<ResponseGroupCreationData> {
                        override fun onFailure(
                            call: Call<ResponseGroupCreationData>,
                            t: Throwable
                        ) {
                            // 통신 실패 로직
                        }

                        override fun onResponse(
                            call: Call<ResponseGroupCreationData>,
                            response: Response<ResponseGroupCreationData>
                        ) {
                            response.takeIf { it.isSuccessful }
                                ?.body()
                                ?.let { it ->
                                    data = response.body()
                                    val group_image = data!!.data
                                    //3-1. 데이터 이미지 string 넘기기
                                    val intent = Intent(
                                        this@GroupCreation4Activity,
                                        GroupCreationCompleteActivity::class.java
                                    )
                                    intent.putExtra("group_image", group_image)
                                    Log.d("4Activity", group_image)

                                    // 3-2. 데이터 전달 하기
                                    intent.putExtra(
                                        "group_name",
                                        secondIntent.getStringExtra("group_name")
                                    )
                                    intent.putExtra(
                                        "recruit_num",
                                        secondIntent.getIntExtra("recruit_num", 0)
                                    )
                                    intent.putExtra(
                                        "is_public",
                                        secondIntent.getBooleanExtra("is_public", false)
                                    )
                                    intent.putExtra(
                                        "intro_comment",
                                        secondIntent.getStringExtra("intro_comment")
                                    )
                                    intent.putExtra("ex_cycle", ex_cycle)
                                    intent.putExtra("ex_intensity", ex_intensity)
                                    intent.putExtra("group_sport", group_sport)

                                    // 4. 다음 액티비티 불러오기
                                    startActivity(intent)

                                    Log.v("group_creation", "성공!")
                                } ?: showError(response.errorBody())
                        }
                    })
                }
            }
        }
    }

    // 서버 연동관련 에러 함수
    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Toast.makeText(this, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }
}