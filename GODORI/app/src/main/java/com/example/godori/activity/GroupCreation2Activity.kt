package com.example.godori.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.godori.R

class GroupCreation2Activity : AppCompatActivity() {
    var recruit_num: Int = 0
    var is_public: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation2)

        // xml 요소
        val next: Button = findViewById(R.id.gr_btn_creation2_next)
        val back: ImageButton = findViewById(R.id.gr_btn_creation2_back)
        val switch: Switch = findViewById(R.id.gr_switch_creation2)
        val swTv: TextView = findViewById(R.id.gr_tv_creation2_subtitle2)
        val num: TextView = findViewById(R.id.gr_et_creation2_num)
        var numS = num.text.toString()

        // 이전
        back.setOnClickListener {
            onBackPressed()
        }

        // switch on/off
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch is enabled/checked
                swTv.text = "모집중인 그룹 목록에 표시됩니다"
                is_public = true
            } else {
                // The switch is disabled
                swTv.text = "그룹 검색을 통해서만 가입할 수 있습니다"
                is_public = false
            }
        }

        // 인원수 입력해야 '다음'버튼 가능
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                next.isEnabled = true
                recruit_num = Integer.parseInt(num.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                next.isEnabled = true
            }
        }
        num.addTextChangedListener(textWatcher)

        //다음
        next.setOnClickListener {
            // 이름 2글자 이상 filter
            numS = num.text.toString()
            var numI = Integer.parseInt(numS)
            if (numI > 20) {
                Toast.makeText(this, "그룹 인원은 20인 이하 가능합니다.", Toast.LENGTH_SHORT).show()
            } else if (numI < 2) {
                Toast.makeText(this, "그룹 인원은 2인 이상 가능합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, GroupCreation3Activity::class.java)
                // 데이터 전달
                val secondIntent = getIntent()
//                intent.putExtra("imagesByte", secondIntent.getByteArrayExtra("imagesByte"))
                intent.putExtra("group_name", secondIntent.getStringExtra("group_name"))
                intent.putExtra("recruit_num", recruit_num)
                intent.putExtra("is_public", is_public)
                // 액티비티 시작
                startActivity(intent)
            }
        }
    }
}