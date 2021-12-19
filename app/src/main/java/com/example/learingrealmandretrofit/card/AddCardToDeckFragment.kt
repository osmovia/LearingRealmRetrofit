package com.example.learingrealmandretrofit.card

import android.os.Bundle
import android.view.*
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

        viewModelFactory = AddCardToDeckViewModelFactory(token = arguments.token, cardId = arguments.cardId)

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

        viewModel.gelAllDecksRealm.observe(viewLifecycleOwner, Observer { allDecks ->
            val adapter = RecyclerAdapterDeckCheckbox(this, allDecks, autoUpdate = true)
            binding.recyclerDeck.layoutManager = LinearLayoutManager(context)
            binding.recyclerDeck.adapter = adapter
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.selectItem) {
            viewModel.addCardToDeckRetrofit()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_select, menu)
    }

    fun onCheckboxClick(isChecked: Boolean, deck: Deck) {
        viewModel.changeStateCheckCheckbox(isChecked, deck)
    }

}
