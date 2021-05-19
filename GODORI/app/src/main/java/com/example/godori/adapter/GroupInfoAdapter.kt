package com.example.godori.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.godori.R

class groupInfoAdapter :
    RecyclerView.Adapter<groupInfoAdapter.groupInfoViewHolder>() {
        //
        //    // Provide a reference to the views for each data item
        //    // Complex data items may need more than one view per item, and
        //    // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class groupInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            public var itemimage: ImageView = itemView.findViewById(R.id.favoriteExercise_img)
            public var itemtitle: TextView = itemView.findViewById(R.id.favoriteExercise_tv)
        }


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): groupInfoViewHolder {
            // create a new view
            val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.favorite_excercise_card_layout, parent, false)

            return groupInfoViewHolder(cardView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: groupInfoViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.itemimage.setImageResource(R.drawable.ic_notice)
            holder.itemtitle.setText("런닝")
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount(): Int {
            return 2
        }
}