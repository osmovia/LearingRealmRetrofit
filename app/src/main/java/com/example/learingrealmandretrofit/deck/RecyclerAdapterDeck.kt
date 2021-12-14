package com.example.learingrealmandretrofit.deck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.R
import com.google.android.material.checkbox.MaterialCheckBox
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class RecyclerAdapterDeck(
    private val owner: DeckFragment?,
    private val deckList: OrderedRealmCollection<Deck>,
    private val visibility: Boolean,
    autoUpdate: Boolean
) : RealmRecyclerViewAdapter<Deck, RecyclerAdapterDeck.DeckViewHolder>(deckList, autoUpdate) {

    override fun getItemCount(): Int = deckList.size

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val item = deckList[position]
        holder.textViewDeckName.text = item.title
        holder.checkBox.isVisible = visibility
        holder.itemContainer.setOnClickListener {
            owner?.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_deck_recycler, parent, false)
        return DeckViewHolder(itemView)
    }

    class DeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewDeckName: TextView = itemView.findViewById(R.id.deck_name)
        val itemContainer: ViewGroup = itemView.findViewById(R.id.itemContainerDeck)
        val checkBox: MaterialCheckBox = itemView.findViewById(R.id.checkbox)
    }
}
