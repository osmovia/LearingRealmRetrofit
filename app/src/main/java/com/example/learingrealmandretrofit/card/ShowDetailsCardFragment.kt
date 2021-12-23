package com.example.learingrealmandretrofit.card

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.viewmodel.ShowDetailsCardViewModel
import com.example.learingrealmandretrofit.card.viewmodel.factory.ShowDetailsCardViewModelFactory
import com.example.learingrealmandretrofit.databinding.ShowDetailsCardBinding
import com.example.learingrealmandretrofit.objects.CardParameters

class ShowDetailsCardFragment : Fragment() {

    private lateinit var binding: ShowDetailsCardBinding
    private lateinit var viewModelFactory: ShowDetailsCardViewModelFactory
    private lateinit var viewModel: ShowDetailsCardViewModel
    private val arguments: ShowDetailsCardFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShowDetailsCardBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModelFactory = ShowDetailsCardViewModelFactory(token = arguments.token, cardId = arguments.cardId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ShowDetailsCardViewModel::class.java)

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

        binding.buttonChangeCard.setOnClickListener {
            viewModel.changeState(true)
        }

        viewModel.showDetailsCard.observe(viewLifecycleOwner, Observer { card ->
            binding.wordId.setText(card.word)
            binding.examplesId.setText(card.example)
            binding.translationId.setText(card.translation)
            (requireActivity() as MainActivity).supportActionBar?.title = card.word
        })

        viewModel.stateView.observe(viewLifecycleOwner, Observer { clickable ->
            binding.wordId.isEnabled = clickable
            binding.translationId.isEnabled = clickable
            binding.examplesId.isEnabled = clickable
            binding.buttonChangeCard.isVisible = !clickable
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_select, menu)
        val iconSelect = menu.findItem(R.id.selectItem)

        viewModel.stateView.observe(viewLifecycleOwner, Observer { showIcon ->
            iconSelect.isVisible = showIcon
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.selectItem) {
            viewModel.changeCard(
                CardParameters(
                    id = arguments.cardId,
                    word = binding.wordId.text.toString(),
                    translation = binding.translationId.text.toString(),
                    example = binding.examplesId.text.toString()
                )
            )
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
