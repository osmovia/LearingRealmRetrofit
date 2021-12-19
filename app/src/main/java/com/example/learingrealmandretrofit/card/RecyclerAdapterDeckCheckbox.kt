package com.example.learingrealmandretrofit.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.deck.Deck
import com.google.android.material.checkbox.MaterialCheckBox
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class RecyclerAdapterDeckCheckbox(
    private val owner: AddCardToDeckFragment?,
    private val deckList: OrderedRealmCollection<Deck>,
    autoUpdate: Boolean
) : RealmRecyclerViewAdapter<Deck, RecyclerAdapterDeckCheckbox.DeckCheckbox>(deckList, autoUpdate) {

    override fun getItemCount(): Int = deckList.size

    override fun onBindViewHolder(holder: DeckCheckbox, position: Int) {
        val item = deckList[position]
        holder.checkBox.isVisible = true
        holder.textViewDeckName.text = item.title

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            owner?.onCheckboxClick(isChecked, item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckCheckbox {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_deck_recycler, parent, false)
        return  DeckCheckbox(itemView)
    }

    class DeckCheckbox(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewDeckName: TextView = itemView.findViewById(R.id.deck_name)
        val checkBox: MaterialCheckBox = itemView.findViewById(R.id.checkbox)
    }
}
