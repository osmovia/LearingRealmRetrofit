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
import com.example.learingrealmandretrofit.databinding.DialogDeleteCardBinding
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.InsideDeckDeleteCardViewModel
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.factory.InsideDeckDeleteCardViewModelFactory
import com.example.learingrealmandretrofit.showErrorCodeOrStringResource

class InsideDeckDeleteCardDialog: DialogFragment() {

    private lateinit var binding: DialogDeleteCardBinding
    private lateinit var viewModelFactory: InsideDeckDeleteCardViewModelFactory
    private lateinit var viewModel: InsideDeckDeleteCardViewModel
    private val arguments: InsideDeckDeleteCardDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteCardBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModelFactory = InsideDeckDeleteCardViewModelFactory(
            application = requireActivity().application,
            cardDeckId = arguments.cardDeckId
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(InsideDeckDeleteCardViewModel::class.java)

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            binding.containerSpinner.layoutProgress.isVisible = showSpinner
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().popBackStack()
            }
        })

        binding.buttonYes.setOnClickListener {
            viewModel.deleteCardRetrofit()
        }

        binding.buttonNo.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
