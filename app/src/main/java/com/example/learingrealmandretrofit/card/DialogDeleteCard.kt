package com.example.learingrealmandretrofit.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.viewmodel.DeleteCardViewModel
import com.example.learingrealmandretrofit.card.viewmodel.factory.DeleteCardViewModelFactory
import com.example.learingrealmandretrofit.databinding.DialogDeleteCardBinding

class DialogDeleteCard : DialogFragment() {
    //Arguments
    private val arguments: DialogDeleteCardArgs by navArgs()
    //ViewModelFactory
    private lateinit var viewModelFactory: DeleteCardViewModelFactory
    //ViewModel
    private lateinit var viewModel: DeleteCardViewModel
    // Bindings
    private lateinit var binding: DialogDeleteCardBinding
    // Lifecycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteCardBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModelFactory = DeleteCardViewModelFactory(arguments.token, arguments.cardId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(DeleteCardViewModel::class.java)

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            if (showSpinner) {
                requireActivity().showProgress()
            } else {
                requireActivity().hideProgress()
            }
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().popBackStack()
            }
        })

        binding.buttonYes.setOnClickListener {
            viewModel.removeCardRetrofit()
        }
        binding.buttonNo.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
