package com.example.godori.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.godori.R
import kotlinx.android.synthetic.main.activity_on_boarding1.*
import kotlinx.android.synthetic.main.activity_on_boarding1.btn1
import kotlinx.android.synthetic.main.activity_on_boarding2.*
import kotlinx.android.synthetic.main.activity_taste_setting.*

class OnBoarding2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding2)

        val secondIntent = getIntent()
        var appNickname = secondIntent.getStringExtra("app_nickname")

        var sports: String = ""
        var sports_count: Int = 0

        yoga_pila_img.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btn2.isEnabled = true
                when (sports_count) {
                    0 -> {
                        sports = "요가/필테"
                        sports_count += 1
                    }
                    else -> {
                        sports = sports.plus(",요가/필테")
                    }
                }
                sports = "요가/필테"
            }
        }
        health_img.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btn2.isEnabled = true
                when (sports_count) {
                    0 -> {
                        sports = "헬스"
                        sports_count += 1
                    }
                    else -> {
                        sports = sports.plus(",헬스")
                    }
                }
            }
        }
        climb_img.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btn2.isEnabled = true
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
        bicycle_img.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btn2.isEnabled = true
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
        swimming_img.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btn2.isEnabled = true
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
        running_img.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btn2.isEnabled = true
                when (sports_count) {
                    0 -> {
                        sports = "러닝"
                        sports_count += 1
                    }
                    else -> {
                        sports = sports.plus(",러닝")
                    }
                }
            }
        }

        btn2.setOnClickListener {
            val intent = Intent(this, OnBoarding3Activity::class.java)
            //앱에서 설정한 닉네임 보내기
            intent.putExtra("app_nickname", appNickname)
            //운동종목 보내기
            intent.putExtra("user_sport", sports)
            startActivity(intent)
        }
    }
}