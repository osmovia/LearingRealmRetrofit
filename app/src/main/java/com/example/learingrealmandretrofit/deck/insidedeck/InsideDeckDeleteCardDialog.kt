package com.example.learingrealmandretrofit.deck.insidedeck

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
import com.example.learingrealmandretrofit.databinding.DialogDeleteCardOrDeleteFromListBinding
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.InsideDeckDeleteCardViewModel
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.factory.InsideDeckDeleteCardViewModelFactory
import com.example.learingrealmandretrofit.hideProgress
import com.example.learingrealmandretrofit.showErrorCodeOrStringResource
import com.example.learingrealmandretrofit.showProgress

class InsideDeckDeleteCardDialog: DialogFragment() {

    private lateinit var binding: DialogDeleteCardOrDeleteFromListBinding
    private lateinit var viewModelFactory: InsideDeckDeleteCardViewModelFactory
    private lateinit var viewModel: InsideDeckDeleteCardViewModel
    private val arguments: InsideDeckDeleteCardDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteCardOrDeleteFromListBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModelFactory = InsideDeckDeleteCardViewModelFactory(
            token = arguments.token,
            deckId = arguments.deckId,
            cardId = arguments.cardId
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(InsideDeckDeleteCardViewModel::class.java)

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

        binding.deleteCardFromList.setOnClickListener {
            viewModel.deleteCardFromListRetrofit()

        }

        binding.deleteCard.setOnClickListener {
            viewModel.deleteCardRetrofit()
        }
    }
}
