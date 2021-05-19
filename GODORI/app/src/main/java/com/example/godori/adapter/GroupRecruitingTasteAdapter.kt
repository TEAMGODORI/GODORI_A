package com.example.godori.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.godori.R
import com.example.godori.data.ResponseGroupRecruit
import java.util.*
import kotlin.collections.ArrayList

class GroupRecruitingTasteAdapter(val user: ResponseGroupRecruit.Data.User, val context: Context) :
    RecyclerView.Adapter<GroupRecruitingTasteAdapter.MyViewHolder>() {
    //
    //    // Provide a reference to the views for each data item
    //    // Complex data items may need more than one view per item, and
    //    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    //adapter에 User 데이터 넣기
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var tasteText: TextView = itemView.findViewById(R.id.gr_tv_taste_cdv)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupRecruitingTasteAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.gr_cdv_recruiting_taste, parent, false)

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

//        holder.tasteText.setText(list.toString())
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        var sport = user.sports.split(",")
        return sport.size +2
    }
}