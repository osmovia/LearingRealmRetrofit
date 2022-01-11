package com.example.learingrealmandretrofit.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.deck.Deck
import com.google.android.material.checkbox.MaterialCheckBox
import io.realm.RealmList

class RecyclerAdapterDeckCheckbox(
    private val owner: AddCardToDeckFragment?,
    private val deckIds: MutableList<Int>,
    private val decks: RealmList<Deck>,
) : RecyclerView.Adapter<RecyclerAdapterDeckCheckbox.DeckCheckbox>() {

    override fun getItemCount(): Int = decks.size

    override fun onBindViewHolder(holder: DeckCheckbox, position: Int) {
        val deck = decks[position]

        holder.checkBox.isClickable = false
        holder.checkBox.isVisible = true
        holder.textViewDeckName.text = deck?.title
        holder.checkBox.isChecked = deckIds.contains(deck?.id)

        holder.containerCheckbox.setOnClickListener {
            if (deck != null) {
                owner?.onCheckboxClick(
                    deck = deck,
                    position = position
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckCheckbox {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_deck_recycler, parent, false)
        return  DeckCheckbox(itemView)
    }

    class DeckCheckbox(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewDeckName: TextView = itemView.findViewById(R.id.deck_name)
        val checkBox: MaterialCheckBox = itemView.findViewById(R.id.checkbox)
        val containerCheckbox: ConstraintLayout = itemView.findViewById(R.id.containerCheckbox)
    }
}
