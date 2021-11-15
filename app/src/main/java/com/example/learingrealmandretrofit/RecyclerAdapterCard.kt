package com.example.learingrealmandretrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.fragment.CardFragment
import com.example.learingrealmandretrofit.objects.CardRealm
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class RecyclerAdapterCard(
    private val owner: CardFragment?,
    private val cardList: OrderedRealmCollection<CardRealm>,
    autoUpdate: Boolean
) : RealmRecyclerViewAdapter<CardRealm, RecyclerAdapterCard.CardViewHolder>(cardList, autoUpdate) {

    override fun getItemCount(): Int = cardList.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = cardList[position]
        holder.textViewOriginal.text = item.word
        holder.textViewTranslate.text = item.translation
        holder.textViwExample.text = item.example
        holder.itemContainer.setOnClickListener {
            owner?.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_recycler, parent, false)
        return CardViewHolder(itemView)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewOriginal: TextView = itemView.findViewById(R.id.textViewOriginal)
        var textViewTranslate: TextView = itemView.findViewById(R.id.textViewTranslate)
        var textViwExample: TextView = itemView.findViewById(R.id.textViewExample)
        var itemContainer: ViewGroup = itemView.findViewById(R.id.itemContainerCard)
    }
}