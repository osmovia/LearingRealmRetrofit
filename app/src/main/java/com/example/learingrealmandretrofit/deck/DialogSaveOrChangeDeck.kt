package com.example.learingrealmandretrofit.deck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learingrealmandretrofit.SharedPreferencesManager
import com.example.learingrealmandretrofit.databinding.DialogCreateOrUpdateDeckBinding
import com.example.learingrealmandretrofit.deck.viewmodel.CreateOrChangeDeckViewModel
import com.example.learingrealmandretrofit.showErrorCodeOrStringResource

class DialogCreateOrChangeDeck : DialogFragment() {

    private lateinit var binding: DialogCreateOrUpdateDeckBinding
    private lateinit var viewModel: CreateOrChangeDeckViewModel
    private val arguments: DialogCreateOrChangeDeckArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateOrUpdateDeckBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fillFields()

        viewModel = ViewModelProvider(this).get(CreateOrChangeDeckViewModel::class.java)

        viewModel.token.value = SharedPreferencesManager(requireContext()).fetchAuthentication().sessionToken

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().popBackStack()
            }
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        binding.buttonCreateOrUpdateDeck.setOnClickListener {
            val deck = arguments.deck
            if (deck == null) {
                viewModel.createDeck(binding.editTextTitle.text.toString())
            } else {
                val changeTitle = Deck(
                    id = deck.id,
                    title = binding.editTextTitle.text.toString(),
                    cards = deck.cards
                )
                viewModel.updateDeck(changeTitle)
            }
        }
    }

    private fun fillFields() {
        if (arguments.deck != null) {
            binding.editTextTitle.setText(arguments.deck?.title)
        }
    }
}
