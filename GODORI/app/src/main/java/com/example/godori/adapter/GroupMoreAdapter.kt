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
import com.example.godori.data.ResponseCertiTab
import com.example.godori.data.ResponseGroupBeforeTab
import com.example.godori.data.ResponseGroupRecruit
import com.makeramen.roundedimageview.RoundedImageView

class GroupMoreAdapter(
    var groupList: List<ResponseGroupBeforeTab.Data>?,
    val context: Context?
) :
    RecyclerView.Adapter<GroupMoreAdapter.MyViewHolder>() {
    interface ItemClick
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var groupName : TextView = itemView.findViewById(R.id.gr_tv_more_groupName)
        public var groupPicture : RoundedImageView = itemView.findViewById(R.id.gr_iv_more_title)
        var num : TextView = itemView.findViewById(R.id.gr_tv_people)
        var comment : TextView = itemView.findViewById(R.id.gr_tv_more_groupLine)
        var sport : TextView = itemView.findViewById(R.id.gr_tv_exercise)
        var cycle : TextView = itemView.findViewById(R.id.gr_tv_cycle)
        var intensity : TextView = itemView.findViewById(R.id.gr_tv_intensity)


    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMoreAdapter.MyViewHolder {
       // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.gr_cdv_more_info, parent, false)

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        val groupImgUrl: String? = groupList?.get(position)?.group_image
        holder.groupName.setText(groupList?.get(position)?.group_name)
        if (groupImgUrl != null) {
            if (groupImgUrl.isNotEmpty()) {
                Glide.with(holder.groupPicture.context)
                    .load(groupImgUrl)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.groupPicture)
            }
        }
        else{
            Glide.with(holder.groupPicture.context)
                .load(R.drawable.gr_img_info_title)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.groupPicture)
        }
//        holder.groupPicture.setImageResource(R.drawable.gr_img_info_title)
        holder.num.text = groupList?.get(position)?.recruit_num.toString()
        holder.comment.text = groupList?.get(position)?.intro_comment
        holder.sport.text = groupList?.get(position)?.group_sport
        holder.cycle.text = "주" + groupList?.get(position)?.ex_cycle.toString()+"회"
        holder.intensity.text = groupList?.get(position)?.ex_intensity

        if(itemClick != null)
        {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        var size : Int = 0
        if(groupList!= null && groupList?.size!! < 6){
            size = groupList!!.size
        } else if(groupList!= null && groupList?.size!! >= 6)
            size = 6
        return size
    }
}