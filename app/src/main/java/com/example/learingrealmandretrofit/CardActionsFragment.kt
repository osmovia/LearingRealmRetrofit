package com.example.learingrealmandretrofit

import androidx.fragment.app.Fragment
import com.example.learingrealmandretrofit.card.Card

abstract class CardActionsFragment : Fragment() {

    abstract fun onCardClick(card: Card)
}
