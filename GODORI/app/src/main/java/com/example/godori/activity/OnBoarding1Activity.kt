package com.example.godori.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.godori.R
import kotlinx.android.synthetic.main.activity_group_creation_complete.*
import kotlinx.android.synthetic.main.activity_on_boarding1.*


class OnBoarding1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding1)

        // 데이터 가져오기
        val secondIntent = getIntent()
        var profileImageUrl = secondIntent.getStringExtra("profileImageUrl").toString()

        @GlideModule
        if (profileImageUrl != null) {
            if (profileImageUrl.isNotEmpty()) {
                Glide.with(this@OnBoarding1Activity)
                    .load(profileImageUrl)
                    .circleCrop()
                    .error(android.R.drawable.stat_notify_error)
                    .into(profile_img)

            } else {
                Glide.with(this@OnBoarding1Activity)
                    .load(R.drawable.gr_img_profile_basic)
                    .circleCrop()
                    .error(android.R.drawable.stat_notify_error)
                    .into(profile_img)
            }
        }

        //닉네임이 있을때만 버튼 활성화
        nickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length > 0) {
                    btn1.isClickable = true
                    btn1.isEnabled = true
                    btn1.setOnClickListener {
                        val intent = Intent(this@OnBoarding1Activity, OnBoarding2Activity::class.java)
                        intent.putExtra("app_nickname", nickname.text.toString())
                        startActivity(intent)
                    }
                } else {
                    btn1.isClickable = false
                }
            }
        })
    }
}