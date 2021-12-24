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
import com.example.learingrealmandretrofit.databinding.DialogDeleteDeckBinding
import com.example.learingrealmandretrofit.deck.viewmodel.DeleteDeckViewModel
import com.example.learingrealmandretrofit.deck.viewmodel.factory.DeleteDeckViewModelFactory
import com.example.learingrealmandretrofit.showErrorCodeOrStringResource

class DialogDeleteDeck : DialogFragment() {

    private lateinit var binding: DialogDeleteDeckBinding

    private lateinit var viewModelFactory: DeleteDeckViewModelFactory

    private lateinit var viewModel: DeleteDeckViewModel

    private val arguments: DialogDeleteDeckArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteDeckBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModelFactory = DeleteDeckViewModelFactory(
            token = arguments.token,
            deckId = arguments.deckId
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(DeleteDeckViewModel::class.java)

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().popBackStack()
            }
        })

        binding.viewDeleteList.setOnClickListener {
            viewModel.deleteDeckRetrofit(withCards = false)
        }

        binding.viewDeleteListWithCards.setOnClickListener {
            viewModel.deleteDeckRetrofit(withCards = true)
        }
    }
}
