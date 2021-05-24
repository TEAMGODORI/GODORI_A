package com.example.godori.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.godori.R
import com.example.godori.data.ResponseGroupAfterTab
import com.makeramen.roundedimageview.RoundedImageView

class GroupTodayCertiAdapter(
    val group: ResponseGroupAfterTab?,
    val todayMemberList: List<ResponseGroupAfterTab.Data.TodayMember>?,
    val context: Context?
) :
    RecyclerView.Adapter<GroupTodayCertiAdapter.MyViewHolder>() {
    //
    //    // Provide a reference to the views for each data item
    //    // Complex data items may need more than one view per item, and
    //    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var name: TextView = itemView.findViewById(R.id.gr_tv_today_name)
        var count: TextView = itemView.findViewById(R.id.gr_tv_count)
        var totalCount: TextView = itemView.findViewById(R.id.gr_tv_total_count)
        var userImg : RoundedImageView = itemView.findViewById(R.id.gr_iv_more_title)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupTodayCertiAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.gr_cdv_today_certi, parent, false)

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        val userImgUrl: String = todayMemberList?.get(position)!!.user_img

        if (userImgUrl.length > 0) {
            Glide.with(holder.userImg.context)
                .load(userImgUrl)
                .circleCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(holder.userImg)

        } else {
            Glide.with(holder.userImg.context)
                .load(R.drawable.gr_img_profile_basic)
                .circleCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(holder.userImg)
        }

        holder.name.setText(todayMemberList!![position].user_name)
        holder.count.setText(todayMemberList[position].week_count.toString())
        holder.totalCount.setText("/" + group!!.data.group_cycle.toString())

        val userImgUrl: String = todayMemberList?.get(position)!!.user_img

        if (userImgUrl.length > 0) {
            Glide.with(holder.userImg.context)
                .load(userImgUrl)
                .circleCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(holder.userImg)

        } else {
            Glide.with(holder.userImg.context)
                .load(R.drawable.gr_img_profile_basic)
                .circleCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(holder.userImg)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        var size : Int = 0
        if (todayMemberList != null) {
           size = todayMemberList.size
        }
        return size
    }
}