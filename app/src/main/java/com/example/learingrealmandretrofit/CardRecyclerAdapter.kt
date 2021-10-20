package com.example.learingrealmandretrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardRecyclerAdapter(
    private val owner: RecyclerWordFragment?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var generalArray: MutableList<Any> = arrayListOf()

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewOriginal: TextView = itemView.findViewById(R.id.textViewOriginal)
        var textViewTranslate: TextView = itemView.findViewById(R.id.textViewTranslate)
        var textViwExample: TextView = itemView.findViewById(R.id.textViewExample)
        var itemContainer: ViewGroup = itemView.findViewById(R.id.itemContainer)
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var header: TextView = itemView.findViewById(R.id.headerTitle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val headerView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header_title, parent, false)
                HeaderViewHolder(headerView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_card_recycler, parent, false)
                ItemViewHolder(itemView)

            }
        }
    }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is HeaderViewHolder -> {
                val item = generalArray.get(position) as String
                holder.header.text = item
            }
            is ItemViewHolder -> {
                val item = generalArray.get(position) as RealmCard
                holder.textViewOriginal.text = item.word
                holder.textViewTranslate.text = item.translate
                holder.textViwExample.text = item.example

                holder.itemContainer.setOnClickListener {
                    owner?.onItemClick(item)
                }
            }
        }
    }

    override fun getItemCount() = generalArray.size

    override fun getItemViewType(position: Int): Int {
        return when (generalArray[position]) {
            is String -> 0
            else -> 1
        }
    }
    fun setWords(cardDataList: MutableList<RealmCard>) {
        generalArray = owner?.buildGeneralArray(cardDataList) ?: arrayListOf()
        notifyDataSetChanged()
    }
}