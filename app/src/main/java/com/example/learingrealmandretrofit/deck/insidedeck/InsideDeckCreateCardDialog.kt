package com.example.learingrealmandretrofit.deck.insidedeck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learingrealmandretrofit.databinding.DialogCreateCardBinding
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.InsideDeckCreateCardViewModel
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.factory.InsideDeckCreateCardViewModelFactory
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.showErrorCodeOrStringResource

class InsideDeckCreateCardDialog : DialogFragment() {

    private lateinit var binding: DialogCreateCardBinding
    private lateinit var viewModel: InsideDeckCreateCardViewModel
    private lateinit var viewModelFactory: InsideDeckCreateCardViewModelFactory
    private val arguments: InsideDeckCreateCardDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateCardBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModelFactory = InsideDeckCreateCardViewModelFactory(
            application = requireActivity().application,
            deckId = arguments.deckId
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(InsideDeckCreateCardViewModel::class.java)

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().popBackStack()
            }
        })

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            binding.containerSpinner.layoutProgress.isVisible = showSpinner
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        binding.buttonSaveCard.setOnClickListener {
                val cardParameters = CardParameters(
                    word = binding.editTextOriginalWord.text.toString(),
                    example = binding.editTextExample.text.toString(),
                    translation = binding.editTextTranslateWord.text.toString()
                )
                viewModel.createCardRetrofit(cardParameters)
        }
    }
}
