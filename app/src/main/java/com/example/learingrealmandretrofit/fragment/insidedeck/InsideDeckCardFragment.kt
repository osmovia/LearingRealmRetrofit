package com.example.learingrealmandretrofit.fragment.insidedeck

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learingrealmandretrofit.MainActivity
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.databinding.CardFragmentRecyclerBinding

class InsideDeckCardFragment : Fragment() {
    private lateinit var binding: CardFragmentRecyclerBinding
    private val args: InsideDeckCardFragmentArgs by navArgs()

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
        val title = args.deck?.title
        (requireActivity() as MainActivity).supportActionBar?.title = title
        binding.buttonCreateCard.setOnClickListener {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_rename, menu)
    }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.renameItem) {
            findNavController().navigate(R.id.action_insideDeckCardFragment_to_dialogCreateOrChangeDeck)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}