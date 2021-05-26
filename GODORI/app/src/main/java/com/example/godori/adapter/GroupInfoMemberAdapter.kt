package com.example.godori.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.godori.R
import com.example.godori.data.ResponseGroupAfterTab
import com.example.godori.data.ResponseGroupInfoAfter
import com.makeramen.roundedimageview.RoundedImageView
import de.hdodenhof.circleimageview.CircleImageView

class GroupInfoMemberAdapter(
    val memberList: List<ResponseGroupInfoAfter.Data.GroupMember>?,
    val context: Context?
) :
    RecyclerView.Adapter<GroupInfoMemberAdapter.MyViewHolder>() {
    //
    //    // Provide a reference to the views for each data item
    //    // Complex data items may need more than one view per item, and
    //    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var name : TextView = itemView.findViewById(R.id.gr_tv_member_name)
        public var profile : CircleImageView = itemView.findViewById(R.id.gr_iv_member_profile)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GroupInfoMemberAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.gr_cdv_info_member, parent, false)

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.text= memberList!![position].name

        val userImgUrl: String = memberList?.get(position)!!.profile_img

        if (userImgUrl.length > 0) {
            Glide.with(holder.profile.context)
                .load(userImgUrl)
                .circleCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(holder.profile)

        } else {
            Glide.with(holder.profile.context)
                .load(R.drawable.gr_img_profile_basic)
                .circleCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(holder.profile)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        var size : Int = 0
        if (memberList != null) {
            size = memberList.size
        }
        return size
    }
}