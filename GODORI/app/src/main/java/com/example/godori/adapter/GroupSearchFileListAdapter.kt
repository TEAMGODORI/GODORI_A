package com.example.godori.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.godori.R
import com.example.godori.activity.GroupSearchActivity
import com.example.godori.data.ResponseGroupSearch

class GroupSearchFileListAdapter internal constructor(
    private var list: List<ResponseGroupSearch.Data>,
    private val listener: GroupSearchActivity
) : RecyclerView.Adapter<GroupSearchFileListAdapter.SearchViewHolder>(), Filterable {
    interface ItemClick
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick: GroupSearchFileListAdapter.ItemClick? = null

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.gr_tv_search_result)
        val iconImageView: ImageButton = itemView.findViewById(R.id.gr_btn_search_result)

        init {
            itemView.setOnClickListener {
            }
        }
    }

    var searchableList: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.gr_cdv_search, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.titleTextView.text = searchableList[position]

        if(itemClick != null)
        {
            var groupId = 0
            for (name in list) {
                if (name.group_name == searchableList[position]) {
                    groupId = name.id
                }
            }
            holder.iconImageView.setOnClickListener { v ->
                itemClick?.onClick(v, groupId)
            }
        }
    }

    override fun getItemCount() = searchableList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                Log.v("SEARCH_charString", charString)
                Log.v("SEARCH_list", list.toString())
                val filteredList = ArrayList<String>()
                if (charString.isEmpty()) {
                    searchableList = list as ArrayList<String>
                } else {
                    for (name in list) {
                        if (name.group_name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(name.group_name)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchableList = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }
        }
    }
}