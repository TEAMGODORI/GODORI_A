package com.example.godori.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.example.godori.R


class GroupCreation3Activity : AppCompatActivity() {
    var intro_comment: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation3)

        // xml 요소
        val next: Button = findViewById(R.id.gr_btn_creation3_next)
        val back: ImageButton = findViewById(R.id.gr_btn_creation3_back)
        val line: EditText = findViewById(R.id.gr_et_creation3_line)
        var lineS = line.text.toString()

        // 이전
        back.setOnClickListener {
            onBackPressed()
        }

        // editText 글자수 세기 (num 변경 & 다음 버튼 색 변경)
        var textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                lineS = line.text.toString()
                next.isEnabled = true
                intro_comment = lineS
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                next.isEnabled = false
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                next.isEnabled = true
            }
        }
        line.addTextChangedListener(textWatcher)

        next.setOnClickListener {
            val intent = Intent(this, GroupCreation4Activity::class.java)
            // 데이터 전달
            val secondIntent = getIntent()
            intent.putExtra("group_name", secondIntent.getStringExtra("group_name"))
            intent.putExtra("recruit_num", secondIntent.getIntExtra("recruit_num",0))
            intent.putExtra("is_public", secondIntent.getBooleanExtra("is_public", false))
            intent.putExtra("intro_comment", intro_comment)
            // 액티비티 시작
            startActivity(intent)
        }
    }
}