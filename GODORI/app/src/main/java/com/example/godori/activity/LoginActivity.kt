package com.example.godori.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.data.ResponseFirstLogin
import com.example.godori.data.ResponseGroupAfterTab
import com.example.godori.fragment.GroupTabFragment
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_group_after_tab.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //카카오톡 로그인 해시키
        var keyHash = Utility.getKeyHash(this)
        Log.d("KEY_HASH", keyHash)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.d("LoginActivity", "토큰 정보 보기 실패")
            }
            else if (tokenInfo != null) {
                Log.d("LoginActivity", "토큰 정보 보기 성공" +
                        "\n회원번호: ${tokenInfo.id}" +
                        "\n만료시간: ${tokenInfo.expiresIn} 초")

                val intent = Intent(this, TabBarActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }

        // 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
                            .show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT)
                            .show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                // 사용자 정보 요청 (기본)
                UserApiClient.instance.me { user, error ->
                    Log.d("LoginActivity", "회원번호: ${user?.id}")
                    Log.d("LoginActivity", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
                    Log.d(
                        "LoginActivity",
                        "프로필 링크: ${user?.kakaoAccount?.profile?.profileImageUrl}"
                    )
                    Log.d(
                        "LoginActivity",
                        "썸네일 링크: ${user?.kakaoAccount?.profile?.thumbnailImageUrl}"
                    )

                    //서버 연동하기 - Callback 등록하여 통신 요청
                    val call: Call<ResponseFirstLogin> =
                        GroupRetrofitServiceImpl.service_lg_first.requestList(
                            kakaoId = user?.id!!
                        )
                    call.enqueue(object : Callback<ResponseFirstLogin> {
                        override fun onFailure(call: Call<ResponseFirstLogin>, t: Throwable) {
                            // 통신 실패 로직
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onResponse(
                            call: Call<ResponseFirstLogin>,
                            response: Response<ResponseFirstLogin>
                        ) {
                            response.takeIf { it.isSuccessful }
                                ?.body()
                                ?.let { it ->
                                    var data = response.body()
                                    Log.d("LoginActivity", data.toString())

                                    var message = it.message
                                    Log.d("LoginActivity", message)

                                    when (message) {
                                        "첫 로그인 입니다" -> {
                                            //message = 첫 로그인일때 튜토리얼
                                            val intent = Intent(
                                                this@LoginActivity,
                                                OnBoarding1Activity::class.java
                                            )
                                            intent.putExtra(
                                                "profileImageUrl",
                                                user?.kakaoAccount?.profile?.profileImageUrl
                                            )
                                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                        }
                                        else -> {
                                            //첫 로그인이 아닐때
                                            val intent = Intent(
                                                this@LoginActivity,
                                                TabBarActivity::class.java
                                            )
                                            intent.putExtra("kakaoId", data!!.data)
                                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                        }
                                    }
//
//                                    todayMemberList = dataList!!.data.today_member
//                                    Log.d("GroupAfterTabFragment", todayMemberList.toString())
//                                    unTodayMemberList = dataList!!.data.not_today_member
//                                    Log.d("GroupAfterTabFragment", unTodayMemberList.toString())
//
//                                    //어댑터에 데이터 넣기
//                                    setGroupCertiedAdapter(todayMemberList!!)
//                                    setGroupCertiAdapter(unTodayMemberList!!)
//
//                                    group_name.setText(it.data.group_name)
//                                    left_count.setText(it.data.left_count.toString())

                                } ?: showError(response.errorBody())
                        }
                    })
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        kakao_login_button.setOnClickListener {
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Toast.makeText(this, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }
}