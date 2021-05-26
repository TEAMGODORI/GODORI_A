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

class GroupAlreadyCertiAdapter(
    val group: ResponseGroupAfterTab?,
    val unTodayMemberList: List<ResponseGroupAfterTab.Data.NotTodayMember>?,
    val context: Context?
) :
    RecyclerView.Adapter<GroupAlreadyCertiAdapter.MyViewHolder>() {
    //
    //    // Provide a reference to the views for each data item
    //    // Complex data items may need more than one view per item, and
    //    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.gr_tv_already_name)
        var count: TextView = itemView.findViewById(R.id.gr_tv_already_count)
        var totalCount: TextView = itemView.findViewById(R.id.gr_tv_already_total_count)
        var userImg : RoundedImageView = itemView.findViewById(R.id.gr_iv_more_title)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupAlreadyCertiAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.gr_cdv_already_certi, parent, false)

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        val userImgUrl: String = unTodayMemberList?.get(position)!!.user_img

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
        holder.name.setText(unTodayMemberList!![position].user_name)
        holder.count.setText(unTodayMemberList[position].week_count.toString())
        holder.totalCount.setText("/" + group!!.data.group_cycle.toString())
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        var size : Int = 0
        if (unTodayMemberList != null) {
            size = unTodayMemberList.size
        }
        return size
    }
}