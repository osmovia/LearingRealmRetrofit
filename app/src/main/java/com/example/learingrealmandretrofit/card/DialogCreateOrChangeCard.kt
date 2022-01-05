package com.example.learingrealmandretrofit.card

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
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.viewmodel.CreateOrChangeCardViewModel
import com.example.learingrealmandretrofit.card.viewmodel.factory.CreateOrChangeCardViewModelFactory
import com.example.learingrealmandretrofit.databinding.DialogCreateOrChangeCardBinding
import com.example.learingrealmandretrofit.objects.CardParameters

class DialogCreateOrChangeCard : DialogFragment() {

    private val arguments: DialogCreateOrChangeCardArgs by navArgs()
    private lateinit var viewModel: CreateOrChangeCardViewModel
    private lateinit var viewModelFactory: CreateOrChangeCardViewModelFactory
    private lateinit var binding: DialogCreateOrChangeCardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateOrChangeCardBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fillFields()

        viewModelFactory = CreateOrChangeCardViewModelFactory(
            token = arguments.token
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(CreateOrChangeCardViewModel::class.java)

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

        binding.buttonCreateOrUpdateCard.setOnClickListener {
            if (arguments.card == null) {
                val cardParameters = CardParameters(
                    word = binding.editTextOriginalWord.text.toString(),
                    example = binding.editTextExample.text.toString(),
                    translation = binding.editTextTranslateWord.text.toString()
                )
                viewModel.createCardRetrofit(cardParameters)
            } else {
                val cardId = arguments.card?.id ?: return@setOnClickListener
                val cardParameters = CardParameters(
                    id = cardId,
                    word = binding.editTextOriginalWord.text.toString(),
                    example = binding.editTextExample.text.toString(),
                    translation = binding.editTextTranslateWord.text.toString()
                )
                viewModel.updateCardRetrofit(cardParameters)
            }
        }
    }

    private fun fillFields() {
        if (arguments.card != null) {
            binding.editTextOriginalWord.setText(arguments.card?.word.toString())
            binding.editTextTranslateWord.setText(arguments.card?.translation.toString())
            binding.editTextExample.setText(arguments.card?.example.toString())
        }
    }
}
