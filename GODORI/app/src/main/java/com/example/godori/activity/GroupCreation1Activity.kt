package com.example.godori.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.example.godori.*
import kotlinx.android.synthetic.main.activity_group_creation1.*
import java.lang.Boolean.TRUE

class GroupCreation1Activity : AppCompatActivity() {
    // 데이터 목록
    var group_name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation1)

        //xml 요소
        val next: Button = findViewById(R.id.gr_btn_creation1_next)
        val back: ImageButton = findViewById(R.id.gr_btn_creation1_back)
        val name: EditText = findViewById(R.id.gr_et_line)
        val num: TextView = findViewById(R.id.gr_tv_name_num)
        var numS = name.text.toString()

        // editText 글자수 세기 (num 변경 & 다음 버튼 색 변경)
        var textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                numS = name.text.toString()
                num.text = numS.length.toString() + "/15"
                next.isEnabled = true
                group_name = numS
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                num.text = "0/15"
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                numS = name.text.toString()
                num.text = numS.length.toString() + "/15"
                next.isEnabled = true
            }
        }
        name.addTextChangedListener(textWatcher)

        // 다음
        gr_btn_creation1_next.setOnClickListener {
            // 이름 2글자 이상 filter
//            numS = name.text.toString()
//            if (numS.length < 3){
//                val layout = layoutInflater.inflate(R.layout.toast_group)
//                val myToast = Toast(applicationContext)
//                myToast.duration = Toast.LENGTH_SHORT
//                myToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
//                myToast.view = layout//setting the view of custom toast layout
//                myToast.show()
//            } else {


            val intent = Intent(this, GroupCreation2Activity::class.java)
            // 데이터 전달
            intent.putExtra("group_name", group_name)
            startActivity(intent)

//            }
        }
        // 이전
        back.setOnClickListener {
            onBackPressed()
        }
    }
}
