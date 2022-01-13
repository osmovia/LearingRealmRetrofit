package com.example.learingrealmandretrofit.card

import android.os.Bundle
import android.view.*
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.viewmodel.AddCardToDeckViewModel
import com.example.learingrealmandretrofit.card.viewmodel.factory.AddCardToDeckViewModelFactory
import com.example.learingrealmandretrofit.databinding.DeckFragmentRecyclerBinding
import com.example.learingrealmandretrofit.deck.Deck

class AddCardToDeckFragment : Fragment() {

    private lateinit var viewModelFactory: AddCardToDeckViewModelFactory
    private lateinit var viewModel: AddCardToDeckViewModel
    private lateinit var binding: DeckFragmentRecyclerBinding
    private val arguments: AddCardToDeckFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeckFragmentRecyclerBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonCreateDeck.isInvisible = true

        binding.toolbarContainer.toolbarId.inflateMenu(R.menu.toolbar_select)

        binding.toolbarContainer.toolbarId.setOnMenuItemClickListener { itemMenu ->
            when (itemMenu.itemId) {
                R.id.selectItem -> viewModel.addAndRemoveCardFromDecksRetrofit()
            }
            true
        }

        NavigationUI.setupWithNavController(binding.toolbarContainer.toolbarId, findNavController())

        viewModelFactory = AddCardToDeckViewModelFactory(application = requireActivity().application, cardId = arguments.cardId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(AddCardToDeckViewModel::class.java)

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

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().popBackStack()
            }
        })

        viewModel.listsReady.observe(viewLifecycleOwner, Observer { listsReady ->
            if (listsReady) {
                    val adapter = RecyclerAdapterDeckCheckbox(
                        this,
                        deckIds = viewModel.decksIds,
                        decks = viewModel.deck)
                    binding.recyclerDeck.layoutManager = LinearLayoutManager(context)
                    binding.recyclerDeck.adapter = adapter
            }
        })

        viewModel.updateRecycler.observe(viewLifecycleOwner, Observer { position ->
            binding.recyclerDeck.adapter?.notifyItemChanged(position)
        })
    }

    fun onCheckboxClick(deck: Deck, position :Int) {
        viewModel.changeStateCheckCheckbox(deck, position)
    }
}
