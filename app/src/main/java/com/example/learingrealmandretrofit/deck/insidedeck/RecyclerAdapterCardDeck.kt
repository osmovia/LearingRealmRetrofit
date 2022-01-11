package com.example.learingrealmandretrofit.deck.insidedeck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.getRandomColorGradient
import com.example.learingrealmandretrofit.objects.CardDeck
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class RecyclerAdapterCardDeck(
    private val owner: InsideDeckCardFragment?,
    private val cardDeckList: OrderedRealmCollection<CardDeck>,
    autoUpdate: Boolean
) : RealmRecyclerViewAdapter<CardDeck, RecyclerAdapterCardDeck.CardDeckViewHolder>(cardDeckList, autoUpdate) {

    override fun getItemCount(): Int = cardDeckList.size

    override fun onBindViewHolder(holder: CardDeckViewHolder, position: Int) {
        val item = cardDeckList[position].card?.first()
        val gradient = ResourcesCompat.getDrawable(holder.itemView.resources, getRandomColorGradient(position), null)
        holder.cardView.background = gradient
        holder.textViewOriginal.text = item?.word
        holder.textViewTranslate.text = item?.translation
        holder.itemContainer.setOnClickListener {
            if (item != null) {
                owner?.onCardClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDeckViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card_recycler, parent, false)
        return CardDeckViewHolder(itemView)
    }

    class CardDeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewOriginal: TextView = itemView.findViewById(R.id.textViewOriginal)
        var textViewTranslate: TextView = itemView.findViewById(R.id.textViewTranslate)
        var itemContainer: ViewGroup = itemView.findViewById(R.id.itemContainerCard)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}
