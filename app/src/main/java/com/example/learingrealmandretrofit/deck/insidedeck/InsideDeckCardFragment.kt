package com.example.learingrealmandretrofit.deck.insidedeck

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.card.RecyclerAdapterCard
import com.example.learingrealmandretrofit.databinding.CardFragmentRecyclerBinding
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.InsideDeckCardViewModel
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.factory.InsideDeckCardViewModelFactory

class InsideDeckCardFragment : CardActionsFragment() {

    private lateinit var binding: CardFragmentRecyclerBinding
    private lateinit var viewModel: InsideDeckCardViewModel
    private lateinit var viewModelFactory: InsideDeckCardViewModelFactory
    private val arguments: InsideDeckCardFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CardFragmentRecyclerBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Title deck start fragment
        (requireActivity() as MainActivity).supportActionBar?.title = arguments.deckTitle

        // New title if has changed
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(Constants.TITLE_INSIDE_DECK)
            ?.observe(viewLifecycleOwner) { newTitle ->
                (requireActivity() as MainActivity).supportActionBar?.title = newTitle
            }

        viewModelFactory = InsideDeckCardViewModelFactory(
            token = SharedPreferencesManager(requireContext()).fetchAuthentication().sessionToken ?: "",
            deckId = arguments.deckId
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(InsideDeckCardViewModel::class.java)

        viewModel.getAllCardsRealm.observe(viewLifecycleOwner, Observer { allCards ->
            val adapter = RecyclerAdapterCard(this, allCards, true)
            binding.recyclerCard.layoutManager = LinearLayoutManager(context)
            binding.recyclerCard.adapter = adapter
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            if (showSpinner) {
                activity?.showProgress()
            } else {
                activity?.hideProgress()
            }
        })

        val cardDelete = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipe = viewModel.getAllCardsRealm.value?.get(viewHolder.absoluteAdapterPosition)?.id
                if (currentSwipe != null) {
                    val action = InsideDeckCardFragmentDirections
                        .actionInsideDeckCardFragmentToInsideDeckCardDeleteDialog(cardId = currentSwipe, deckId = arguments.deckId)
                    findNavController().navigate(action)
                    binding.recyclerCard.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(cardDelete)
        itemTouchHelper.attachToRecyclerView(binding.recyclerCard)

        binding.buttonCreateCard.setOnClickListener {
            val action = InsideDeckCardFragmentDirections
                .actionInsideDeckCardFragmentToInsideDeckUpdateCardDialog(card = null, deckId = arguments.deckId)
            findNavController().navigate(action)
        }
    }

    override fun onCardClick(card: Card) {
        val action = InsideDeckCardFragmentDirections
            .actionInsideDeckCardFragmentToInsideDeckUpdateCardDialog(card = card, deckId = arguments.deckId)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_rename, menu)
    }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.renameItem) {
            val action = InsideDeckCardFragmentDirections
                .actionInsideDeckCardFragmentToDialogCreateOrChangeDeck(arguments.deckId)
            findNavController().navigate(action)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
