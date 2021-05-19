package com.example.godori.adapter

import android.R.attr.name
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.godori.R
import com.example.godori.activity.GroupInfoActivity
import com.example.godori.adapter.GroupRecruitingInfoAdapter.MyViewHolder
import com.example.godori.data.ResponseGroupRecruit


class GroupRecruitingInfoAdapter(
    val groupList: List<ResponseGroupRecruit.Data.Group>?,
    val context: Context
) :
    RecyclerView.Adapter<MyViewHolder>() {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    interface ItemClick
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var group_sport: TextView = itemView.findViewById(R.id.gr_tv_recruiting_info_categories)
        var created_at: TextView = itemView.findViewById(R.id.gr_tv_recruiting_info_time)
        var group_name: TextView = itemView.findViewById(R.id.gr_tv_recruiting_info_groupName)
        var intro_comment: TextView = itemView.findViewById(R.id.gr_tv_recruiting_info_line)
        var ex_cycle: TextView = itemView.findViewById(R.id.gr_tv_recruiting_info_cycle)
        var ex_intensity: TextView = itemView.findViewById(R.id.gr_tv_recruiting_info_level)
        var recruited_num: TextView = itemView.findViewById(R.id.gr_tv_recruiting_info_recruited_num)
        var recruit_num: TextView = itemView.findViewById(R.id.gr_tv_recruiting_info_recruit_num)
        var recruit: ImageButton = itemView.findViewById(R.id.gr_ib_recruiting_info_countTitle)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.gr_cdv_recruiting_info, parent, false)

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: GroupRecruitingInfoAdapter.MyViewHolder, position: Int) {
        holder.group_sport.setText(groupList!![position].group_sport)
        holder.created_at.setText(groupList[position].parse_date)
        holder.group_name.setText(groupList[position].group_name)
        holder.intro_comment.setText(groupList[position].intro_comment)
        holder.ex_cycle.setText("주 " + groupList[position].ex_cycle + "회")
        holder.ex_intensity.setText(groupList[position].ex_intensity)
        holder.recruited_num.setText(groupList[position].recruited_num.toString())
        holder.recruit_num.setText("/" + groupList[position].recruit_num.toString())

        if(itemClick != null)
        {
            holder.recruit.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        val size: Int = if (groupList != null && groupList.isNotEmpty()) {
            groupList.size
        } else {
            0
        }
        return size
    }

}