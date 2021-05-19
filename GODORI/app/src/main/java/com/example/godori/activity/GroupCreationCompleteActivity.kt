package com.example.godori.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.godori.R
import com.example.godori.adapter.TabBarViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_group_creation_complete.*
import kotlinx.android.synthetic.main.activity_tab_bar.*
import kotlin.properties.Delegates


class GroupCreationCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation_complete)

        // 데이터 가져오기
        val secondIntent = getIntent()
        var group_name = secondIntent.getStringExtra("group_name").toString()
        var recruit_num = secondIntent.getIntExtra("recruit_num", 0)
        var intro_comment = secondIntent.getStringExtra("intro_comment").toString()
        var group_sport = secondIntent.getStringExtra("group_sport").toString()
        var ex_intensity = secondIntent.getStringExtra("ex_intensity").toString()
        var ex_cycle = secondIntent.getIntExtra("ex_cycle", 0)
        var group_image = secondIntent.getStringExtra("group_image").toString()

        Log.d("CompleteActivity", group_image)

        // 텍스트뷰 셋팅
        gr_tv_creation_groupName.text = group_name
        gr_tv_creation_introComment.text = intro_comment
        gr_tv_exercise.text = group_sport
        gr_tv_creation_cycle.text = "주${ex_cycle}회"
        gr_tv_creation_intensity.text = ex_intensity
        gr_tv_creation_num.text = "${recruit_num}명"

        //그룹 이미지 셋팅
        @GlideModule
        if (group_image != null) {
            if (group_image.isNotEmpty()) {
                Glide.with(this@GroupCreationCompleteActivity)
                    .load(group_image)
                    .error(android.R.drawable.stat_notify_error)
                    .into(gr_iv_creation_title)

            } else {
                Glide.with(this@GroupCreationCompleteActivity)
                    .load(R.drawable.certif_un)
                    .error(android.R.drawable.stat_notify_error)
                    .into(gr_iv_creation_title)
            }
        }

        // 그룹탭 메인으로 돌아가기
        gr_btn_creation_complete.setOnClickListener {
            // 이전 뷰 스택 다 지우고 TabBar 액티비티로 돌아가기
            val intent = Intent(application, TabBarActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(intent)
        }
    }
}