package com.example.learingrealmandretrofit.card

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.viewmodel.CardViewModel
import com.example.learingrealmandretrofit.card.viewmodel.factory.CardViewModelFactory
import com.example.learingrealmandretrofit.databinding.CardFragmentRecyclerBinding

class CardFragment : CardActionsFragment() {

    private lateinit var binding: CardFragmentRecyclerBinding
    private lateinit var viewModelFactory: CardViewModelFactory
    private lateinit var viewModel: CardViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CardFragmentRecyclerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.deckFragment, R.id.cardFragment, R.id.settingFragment))

        NavigationUI.setupWithNavController(binding.toolbarContainer.toolbarId, findNavController(), appBarConfiguration)

        token = SharedPreferencesManager(requireContext()).fetchAuthentication().sessionToken ?: ""

        viewModelFactory = CardViewModelFactory(token = token)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CardViewModel::class.java)

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            if (showSpinner) {
                activity?.showProgress()
            } else {
                activity?.hideProgress()
            }
        })

        viewModel.getAllCardsRealm.observe(viewLifecycleOwner, Observer { allCards ->
            val adapter = RecyclerAdapterCard(this, allCards, true)
            binding.recyclerCard.layoutManager = LinearLayoutManager(context)
            binding.recyclerCard.adapter = adapter
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        binding.buttonCreateCard.setOnClickListener {
            val action = CardFragmentDirections.actionCardFragmentToDialogCreateOrChangeCard(
                card = null,
                token = token
            )
            findNavController().navigate(action)
        }

        val itemDelete = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipeCardId = viewModel.getAllCardsRealm.value?.get(viewHolder.absoluteAdapterPosition)?.id
                if (currentSwipeCardId != null) {
                    val action = CardFragmentDirections.actionCardFragmentToDialogDeleteCard(
                        cardId = currentSwipeCardId,
                        token = token
                    )
                    findNavController().navigate(action)
                    binding.recyclerCard.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
            }
        }
        val itemTouchHelperDelete = ItemTouchHelper(itemDelete)
        itemTouchHelperDelete.attachToRecyclerView(binding.recyclerCard)

        val itemRelationship = object : SwipeToAddRelationship(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipeCardId = viewModel.getAllCardsRealm.value?.get(viewHolder.absoluteAdapterPosition)?.id
                if (currentSwipeCardId != null) {
                    val action = CardFragmentDirections.actionCardFragmentToAddCardToDeckFragment(
                        cardId = currentSwipeCardId,
                        token = token
                    )
                    findNavController().navigate(action)
                    binding.recyclerCard.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
            }
        }
        val itemTouchHelperRelationship = ItemTouchHelper(itemRelationship)
        itemTouchHelperRelationship.attachToRecyclerView(binding.recyclerCard)
    }

    override fun onCardClick(card: Card) {
        val action = CardFragmentDirections.actionCardFragmentToShowDetailsCardFragment(cardId = card.id, token = token)
        findNavController().navigate(action)
    }
}
