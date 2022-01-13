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
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.viewmodel.CreateCardViewModel
import com.example.learingrealmandretrofit.card.viewmodel.factory.CreateCardViewModelFactory
import com.example.learingrealmandretrofit.databinding.DialogCreateCardBinding
import com.example.learingrealmandretrofit.objects.CardParameters

class DialogCreateCard : DialogFragment() {

    private lateinit var viewModel: CreateCardViewModel
    private lateinit var viewModelFactory: CreateCardViewModelFactory
    private lateinit var binding: DialogCreateCardBinding

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

        viewModelFactory = CreateCardViewModelFactory(application = requireActivity().application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CreateCardViewModel::class.java)

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
