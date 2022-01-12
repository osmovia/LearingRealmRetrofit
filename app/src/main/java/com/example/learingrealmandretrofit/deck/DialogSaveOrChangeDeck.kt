package com.example.learingrealmandretrofit.deck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learingrealmandretrofit.Constants
import com.example.learingrealmandretrofit.SharedPreferencesManager
import com.example.learingrealmandretrofit.databinding.DialogCreateOrUpdateDeckBinding
import com.example.learingrealmandretrofit.deck.viewmodel.CreateOrChangeDeckViewModel
import com.example.learingrealmandretrofit.deck.viewmodel.factory.CreateOrChangeDeckViewModelFactory
import com.example.learingrealmandretrofit.showErrorCodeOrStringResource

class DialogCreateOrChangeDeck : DialogFragment() {

    private lateinit var binding: DialogCreateOrUpdateDeckBinding
    private lateinit var viewModelFactory: CreateOrChangeDeckViewModelFactory
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

        viewModelFactory = CreateOrChangeDeckViewModelFactory(token = arguments.token)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CreateOrChangeDeckViewModel::class.java)

        if (arguments.deckId != 0) {
            viewModel.pullDeck(arguments.deckId)
        }

        viewModel.currentDeck.observe(viewLifecycleOwner, Observer { currentDeck ->
                binding.editTextTitle.setText(currentDeck.title)
        })

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            binding.containerSpinner.layoutProgress.isVisible = showSpinner
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            when (success) {
                Constants.CREATE -> {
                    findNavController().popBackStack()
                }
                Constants.UPDATE -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(Constants.TITLE_INSIDE_DECK, viewModel.newTitle.value)
                    findNavController().popBackStack()
                }
            }
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        binding.buttonCreateOrUpdateDeck.setOnClickListener {
            if (arguments.deckId == 0) {
                viewModel.createDeck(binding.editTextTitle.text.toString())
            } else {
                viewModel.updateDeck(binding.editTextTitle.text.toString())
            }
        }
    }
}
