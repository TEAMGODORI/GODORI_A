package com.example.godori.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.godori.R
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setting_btn_back.setOnClickListener {
            onBackPressed()
        }

        logout_layout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.d("SettingActivity", "로그아웃 실패 $error")
                }else {
                    Log.d("SettingActivity", "로그아웃 성공")
                }

                val intent = Intent(this, LoginMainActivity::class.java)

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                startActivity(intent)
            }
        }
    }
}