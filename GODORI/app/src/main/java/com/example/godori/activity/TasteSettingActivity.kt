package com.example.godori.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.data.RequestGroupCreationData
import com.example.godori.data.RequestTasteSetting
import com.example.godori.data.ResponseGroupCreationData
import kotlinx.android.synthetic.main.activity_group_creation4.*
import kotlinx.android.synthetic.main.activity_group_creation4.gr_btn_creation4_next
import kotlinx.android.synthetic.main.activity_group_creation4.gr_cb_creation4_exercise1
import kotlinx.android.synthetic.main.activity_group_creation_complete.*
import kotlinx.android.synthetic.main.activity_taste_setting.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasteSettingActivity : AppCompatActivity() {
    // 데이터 목록
    var ex_cycle: Int = 0
    var ex_intensity: String = ""
    var sports: String = ""
    var sports_count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taste_setting)

        // 다음
        taste_btn_complete.setOnClickListener {

            val call: Call<ResponseGroupCreationData> =
                GroupRetrofitServiceImpl.service_taste.taste(
                    "김지현",
                    RequestTasteSetting(
                        ex_cycle = ex_cycle,
                        ex_intensity = ex_intensity,
                        sports = sports
                    )
                )
            call.enqueue(object : Callback<ResponseGroupCreationData> {
                override fun onFailure(call: Call<ResponseGroupCreationData>, t: Throwable) {
                    // 통신 실패 로직
                }

                override fun onResponse(
                    call: Call<ResponseGroupCreationData>,
                    response: Response<ResponseGroupCreationData>
                ) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let { it ->
                            Log.d("test", response.body().toString())

                            // 전 페이지로 이동
                            onBackPressed()
                        } ?: showError(response.errorBody())
                }
            })
        }

        // 이전
        taste_back.setOnClickListener {
            onBackPressed()
        }

        // 운동 주기
        taste_rg_cycle.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.taste_rb_cycle1 -> ex_cycle = 1
                R.id.taste_rb_cycle2 -> ex_cycle = 2
                R.id.taste_rb_cycle3 -> ex_cycle = 3
                R.id.taste_rb_cycle4 -> ex_cycle = 4
                R.id.taste_rb_cycle5 -> ex_cycle = 5
                R.id.taste_rb_cycle6 -> ex_cycle = 6
                R.id.taste_rb_cycle7 -> ex_cycle = 7
            }
        }

        // 운동 강도
        taste_btn_intensity.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.taste_btn_intensity1 -> ex_intensity = "상"
                R.id.taste_btn_intensity2 -> ex_intensity = "중"
                R.id.taste_btn_intensity3 -> ex_intensity = "하"
            }
        }

        // 운동 선택하면 다음 버튼 활성화
        taste_btn_exercise1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                taste_btn_complete.isEnabled = true
                when (sports_count) {
                    0 -> {
                        sports = "헬스"
                        sports_count += 1
                    }
                    else -> {
                        sports = sports.plus(",헬스")
                    }
                }
                sports = "헬스"
            }
        }
        taste_btn_exercise2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                taste_btn_complete.isEnabled = true
                when (sports_count) {
                    0 -> {
                        sports = "요가/필테"
                        sports_count += 1
                    }
                    else -> {
                        sports = sports.plus(",요가/필테")
                    }
                }
            }
        }
        taste_btn_exercise3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                taste_btn_complete.isEnabled = true
                when (sports_count) {
                    0 -> {
                        sports = "등산"
                        sports_count += 1
                    }
                    else -> {
                        sports = sports.plus(",등산")
                    }
                }
            }
        }
        taste_btn_exercise4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                taste_btn_complete.isEnabled = true
                when (sports_count) {
                    0 -> {
                        sports = "자전거"
                        sports_count += 1
                    }
                    else -> {
                        sports = sports.plus(",자전거")
                    }
                }
            }
        }
        taste_btn_exercise5.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                taste_btn_complete.isEnabled = true
                when (sports_count) {
                    0 -> {
                        sports = "수영"
                        sports_count += 1
                    }
                    else -> {
                        sports = sports.plus(",수영")
                    }
                }
            }
        }
        taste_btn_exercise6.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                taste_btn_complete.isEnabled = true
                when (sports_count) {
                    0 -> {
                        sports = "런닝"
                        sports_count += 1
                    }
                    else -> {
                        sports = sports.plus(",런닝")
                    }
                }
            }
        }
    }

    // 서버 연동관련 에러 함수
    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Log.d("test", e.string())
        Toast.makeText(this, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }
}