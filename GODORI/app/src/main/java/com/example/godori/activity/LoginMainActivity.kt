package com.example.godori.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.adapter.ViewPagerAdapter
import com.example.godori.data.ResponseFirstLogin
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login_main.*
import kotlinx.android.synthetic.main.activity_login_main.kakao_login_button
import me.relex.circleindicator.CircleIndicator3
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginMainActivity : AppCompatActivity() {
    var kakaoId:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)

        viewpager.adapter = ViewPagerAdapter()
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        indicator.setViewPager(viewpager)
        indicator.createIndicators(3, 0)

        //카카오톡 로그인 해시키
        var keyHash = Utility.getKeyHash(this)
        Log.d("KEY_HASH", keyHash)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.d("LoginMainActivity", "토큰 정보 보기 실패")
            }
            else if (tokenInfo != null) {
                Log.d("LoginMainActivity", "토큰 정보 보기 성공" +
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
                        Log.d("LoginMainActivity", "접근이 거부 됨(동의 취소)")
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Log.d("LoginMainActivity", "유효하지 않은 앱")
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Log.d("LoginMainActivity", "인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Log.d("LoginMainActivity", "요청 파라미터 오류")
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Log.d("LoginMainActivity", "유효하지 않은 scope ID")
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Log.d("LoginMainActivity", "설정이 올바르지 않음(android key hash)")
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Log.d("LoginMainActivity",  "서버 내부 에러")
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Log.d("LoginMainActivity", "앱이 요청 권한이 없음")
                    }
                    else -> { // Unknown
                        Log.d("LoginMainActivity", "기타 에러")
                    }
                }
            } else if (token != null) {
                Log.d("LoginMainActivity", "로그인에 성공하였습니다.")

                // 사용자 정보 요청 (기본)
                UserApiClient.instance.me { user, error ->
                    Log.d("LoginMainActivity", "회원번호: ${user?.id}")
                    Log.d("LoginMainActivity", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
                    Log.d(
                        "LoginMainActivity",
                        "프로필 링크: ${user?.kakaoAccount?.profile?.profileImageUrl}"
                    )
                    Log.d(
                        "LoginMainActivity",
                        "썸네일 링크: ${user?.kakaoAccount?.profile?.thumbnailImageUrl}"
                    )

                    //카카오톡 로그인 해시키
                    var keyHash = Utility.getKeyHash(this)
                    Log.d("KEY_HASH", keyHash)

                    UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                        if (error != null) {
                            Log.d("LoginMainActivity", "토큰 정보 보기 실패")
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
                                                this@LoginMainActivity,
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
                                                this@LoginMainActivity,
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

    override fun onBackPressed() {
        if (viewpager.currentItem == 0) {
            // 사용자가 첫 번째 페이지에서 뒤로가기 버튼을 누르면 finish 하도록 하고
            super.onBackPressed()
        } else {
            // 그렇지 않으면 이전 페이지로 이동하게 한다.
            viewpager.currentItem = viewpager.currentItem - 1
        }
    }
}