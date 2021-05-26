package com.example.godori.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.godori.R
import kotlinx.android.synthetic.main.item_page.view.*

class ViewPagerAdapter : RecyclerView.Adapter<PagerVH>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))

    //get the size of color array
    override fun getItemCount(): Int = 3 // Int.MAX_VALUE

    //binding the screen with view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        if(position == 0){
            lg_title_text.text= "규칙적인 운동의 시작\n" +
                                "오늘은 운동해야지"
            lg_text.text = "오늘부터 규칙적인 운동 습관을 길러봐요!\n" +
                           "단, 2주 동안 인증하지 않으면 \n" +
                           "자동으로 그룹에서 탈퇴되니 주의하세요!"
            lg_img.setImageResource(R.drawable.lg_img_illust)
        }
        if(position == 1) {
            lg_title_text.text = "지금 당장\n" +
                                "인증해볼까요?"
            lg_text.text = "운동 목표가 비슷한 그룹원들과\n" +
                            "운동 기록을 공유해보세요."
            lg_img.setImageResource(R.drawable.lg_img_illust2)
        }
        if(position == 2) {
            lg_title_text.text = "자유롭게\n" +
                                "그룹을 선택하세요!"
            lg_text.text = "운동 목표가 바뀌었다면\n" +
                    "다른 그룹에 자유롭게 가입할 수 있어요."
            lg_img.setImageResource(R.drawable.lg_img_illust3) // 수정하기
        }
    }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)