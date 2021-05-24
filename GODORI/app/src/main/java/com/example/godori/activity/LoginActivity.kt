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
    var kakaoId:Long = 0
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
                kakaoId = tokenInfo.id
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        // 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Log.d("LoginActivity", "접근이 거부 됨(동의 취소)")
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Log.d("LoginActivity", "유효하지 않은 앱")
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Log.d("LoginActivity", "인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Log.d("LoginActivity", "요청 파라미터 오류")
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Log.d("LoginActivity", "유효하지 않은 scope ID")
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Log.d("LoginActivity", "설정이 올바르지 않음(android key hash)")
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Log.d("LoginActivity",  "서버 내부 에러")
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Log.d("LoginActivity", "앱이 요청 권한이 없음")
                    }
                    else -> { // Unknown
                        Log.d("LoginActivity", "기타 에러")
                    }
                }
            } else if (token != null) {
                Log.d("LoginActivity", "로그인에 성공하였습니다.")

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

                            kakaoId = tokenInfo.id
                        }
                    }

                    //서버 연동하기 - Callback 등록하여 통신 요청
                    val call: Call<ResponseFirstLogin> =
                        GroupRetrofitServiceImpl.service_lg_first.requestList(
                            kakaoId = kakaoId
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
                                            startActivity(intent)
                                        }
                                        else -> {
                                            //첫 로그인이 아닐때
                                            val intent = Intent(
                                                this@LoginActivity,
                                                TabBarActivity::class.java
                                            )
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                        }
                                    }
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