package com.example.learingrealmandretrofit.deck.insidedeck

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learingrealmandretrofit.Constants
import com.example.learingrealmandretrofit.MainActivity
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.databinding.CardFragmentRecyclerBinding

class InsideDeckCardFragment : Fragment() {
    private lateinit var binding: CardFragmentRecyclerBinding
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

        binding.buttonCreateCard.setOnClickListener {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_rename, menu)
    }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.renameItem) {
            val action = InsideDeckCardFragmentDirections.actionInsideDeckCardFragmentToDialogCreateOrChangeDeck(arguments.deckId)
            findNavController().navigate(action)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
