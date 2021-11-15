package com.example.learingrealmandretrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.fragment.DeckFragment
import com.example.learingrealmandretrofit.objects.DeckRealm
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class RecyclerAdapterDeck (
    private val owner: DeckFragment?,
    private val deckList: OrderedRealmCollection<DeckRealm>,
    autoUpdate: Boolean
) : RealmRecyclerViewAdapter<DeckRealm, RecyclerAdapterDeck.DeckViewHolder>(deckList, autoUpdate) {

    override fun getItemCount(): Int = deckList.size

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val item = deckList[position]
        holder.textViewDeckName.text = item.title
        holder.itemContainer.setOnClickListener {
            owner?.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deck_recycler, parent, false)
        return DeckViewHolder(itemView)
    }


    class DeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewDeckName: TextView = itemView.findViewById(R.id.deck_name)
        val itemContainer: ViewGroup = itemView.findViewById(R.id.itemContainerDeck)
    }
}