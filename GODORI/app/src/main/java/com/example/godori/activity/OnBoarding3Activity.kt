package com.example.godori.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.data.RequestCreateUserInfo
import com.example.godori.data.RequestGroupCreationData
import com.example.godori.data.ResponseCreateUserInfo
import com.example.godori.data.ResponseGroupCreationData
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_on_boarding3.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnBoarding3Activity : AppCompatActivity() {

    var data: ResponseCreateUserInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding3)

        //1, 2에서 데이터 받아오기
        val secondIntent = getIntent()
        var appNickname = secondIntent.getStringExtra("app_nickname").toString()
        var userSports = secondIntent.getStringExtra("user_sport").toString()

        Log.d("OnBoarding3Activity", appNickname)
        Log.d("OnBoarding3Activity", userSports)

        btn3.setOnClickListener {

            // 사용자 정보 요청 (기본)
            UserApiClient.instance.me { user, error ->
                //서버 연동
                val call: Call<ResponseCreateUserInfo> =
                    GroupRetrofitServiceImpl.service_ob_user_creation.requestList(
                        RequestCreateUserInfo(
                            kakao_id = user?.id!!,
                            name = user?.kakaoAccount?.profile?.nickname!!,
                            nickname = appNickname,
                            profile_img = user?.kakaoAccount?.profile?.profileImageUrl!!,
                            user_sport = userSports
                        )
                    )
                call.enqueue(object : Callback<ResponseCreateUserInfo> {
                    override fun onFailure(call: Call<ResponseCreateUserInfo>, t: Throwable) {
                        // 통신 실패 로직
                    }

                    override fun onResponse(
                        call: Call<ResponseCreateUserInfo>,
                        response: Response<ResponseCreateUserInfo>
                    ) {
                        response.takeIf { it.isSuccessful }
                            ?.body()
                            ?.let { it ->
                                data = response.body()
                                Log.d("OnBoarding3Data", data.toString())
                                val appNickname = data!!.data
                                //앱 닉네임이 제대로 응답왔는지 확인
                                Log.d("OnBoarding3ActivityData", appNickname)

                                //탭바 액티비티로 이동
                                val intent = Intent(this@OnBoarding3Activity, TabBarActivity::class.java)
                                // 데이터 전달 하기
                                intent.putExtra("app_nickname", appNickname)
                                intent.putExtra("app_nickname", appNickname)
                                intent.putExtra("profile_image_url", user?.kakaoAccount?.profile?.profileImageUrl!!)

                                // 탭바 액티비티 불러오기
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

                                Log.v("group_creation", "성공!")
                            } ?: showError(response.errorBody())
                    }
                })
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