package com.example.godori.activity

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.godori.fragment.CertifTabFragment
import com.example.godori.R
import kotlinx.android.synthetic.main.activity_certif_tab_upload1.*
import kotlinx.android.synthetic.main.activity_certif_tab_upload2.*
import kotlinx.android.synthetic.main.activity_certif_tab_upload3.*
import kotlinx.android.synthetic.main.activity_group_creation4.*
import kotlinx.android.synthetic.main.activity_taste_setting.*
import java.io.File

class CertifTabUpload3Activity : AppCompatActivity() {

    // 데이터 목록
    var certi_sport: String = ""
    var ex_intensity: String = ""
    var ex_evalu: String = ""


    var sports_count: Int = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
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
        setContentView(R.layout.activity_certif_tab_upload3)

        //백버튼 눌렀을 때
        backBtn3.setOnClickListener {
            onBackPressed()
        }

        // 운동 종목
        exercise_Btn1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (sports_count) {
                    0 -> {
                        certi_sport = "헬스"
                        sports_count += 1
                    }
                    else -> {
                        certi_sport = certi_sport.plus(",헬스")
                    }
                }
            }
        }
        exercise_Btn2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (sports_count) {
                    0 -> {
                        certi_sport = "필라테스"
                        sports_count += 1
                    }
                    else -> {
                        certi_sport = certi_sport.plus(",필라테스")
                    }
                }
            }
        }
        exercise_Btn3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (sports_count) {
                    0 -> {
                        certi_sport = "요가"
                        sports_count += 1
                    }
                    else -> {
                        certi_sport = certi_sport.plus(",요가")
                    }
                }
            }
        }
        exercise_Btn4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (sports_count) {
                    0 -> {
                        certi_sport = "자전거"
                        sports_count += 1
                    }
                    else -> {
                        certi_sport = certi_sport.plus(",자전거")
                    }
                }
            }
        }
        exercise_Btn5.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (sports_count) {
                    0 -> {
                        certi_sport = "수영"
                        sports_count += 1
                    }
                    else -> {
                        certi_sport = certi_sport.plus(",수영")
                    }
                }
            }
        }
        exercise_Btn6.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (sports_count) {
                    0 -> {
                        certi_sport = "런닝"
                        sports_count += 1
                    }
                    else -> {
                        certi_sport = certi_sport.plus(",런닝")
                    }
                }
            }
        }


        // 운동 강도
        intensity_RBtn1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.intensity_Btn1 -> ex_intensity = "살살"
                R.id.intensity_Btn2 -> ex_intensity = "쏘쏘"
                R.id.intensity_Btn3 -> ex_intensity = "빡세"
            }
        }

        // 운동 평가
        reviews_Btn1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ex_evalu = "2% 부족했어요"
                next3Btn.isEnabled = true
            }
        }
        reviews_Btn2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ex_evalu = "근육을 마구 자극했어요"
                next3Btn.isEnabled = true
            }
        }
        reviews_Btn3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ex_evalu = "시원하게 땀 흘렸어요"
                next3Btn.isEnabled = true
            }
        }
        reviews_Btn4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ex_evalu = "쉬엄쉬엄 했어요"
                next3Btn.isEnabled = true
            }
        }
        reviews_Btn5.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ex_evalu = "오늘도 해낸 나에게 칭찬!"
                next3Btn.isEnabled = true
            }
        }
        reviews_Btn6.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ex_evalu = "내일 열심히 할래요"
                next3Btn.isEnabled = true
            }
        }

        // 다음
        next3Btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CertifTabUpload4Activity::class.java)
            // 데이터 전달

            val secondIntent = getIntent()
            intent.putExtra("ex_time", secondIntent.getStringExtra("ex_time"))
            intent.putExtra("imageURI", secondIntent.getStringExtra("imageURI"))
            intent.putExtra("ex_intensity", ex_intensity)
            intent.putExtra("ex_evalu", ex_evalu)
            intent.putExtra("certi_sport", certi_sport)

            // 액티비티 시작
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        })
    }
}