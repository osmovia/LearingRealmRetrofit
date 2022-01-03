package com.example.learingrealmandretrofit.card

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this) {
            viewModel.changeState(true)
        }
    }

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

        NavigationUI.setupWithNavController(binding.toolbarContainer.toolbarId, findNavController())
        
        binding.toolbarContainer.toolbarId.inflateMenu(R.menu.toolbar_select)

        viewModelFactory = ShowDetailsCardViewModelFactory(token = arguments.token, cardId = arguments.cardId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ShowDetailsCardViewModel::class.java)

        viewModel.showAllDecksCard.observe(viewLifecycleOwner, Observer { titels ->
            var allDeckTitle = "Lists: "
            titels?.forEach {
                allDeckTitle += it
            }
            binding.buttonLists.text = allDeckTitle
        })

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
            binding.toolbarContainer.toolbarId.title = card.word
        })

        viewModel.stateView.observe(viewLifecycleOwner, Observer { clickable ->
            binding.wordId.isEnabled = clickable
            binding.translationId.isEnabled = clickable
            binding.examplesId.isEnabled = clickable
            binding.buttonChangeCard.isVisible = !clickable
            binding.toolbarContainer.toolbarId.menu?.findItem(R.id.selectItem)?.isVisible = clickable
        })

        binding.toolbarContainer.toolbarId.setOnMenuItemClickListener { itemMenu ->
            when(itemMenu.itemId) {
                R.id.selectItem -> { viewModel.changeState(false)}
                R.id.home -> { Log.d("KEKA", "Clock back ok!!!") }
            }
            true
        }

    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.toolbar_select, menu)
//        val iconSelect = menu.findItem(R.id.selectItem)
//        iconSelect.isVisible = true
//
//
//        viewModel.stateView.observe(viewLifecycleOwner, Observer { showIcon ->
//            iconSelect.isVisible = showIcon
//        })
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.drawable.ic_baseline_add_48) {
            viewModel.changeState(true)
//            viewModel.changeCard(
//                CardParameters(
//                    id = arguments.cardId,
//                    word = binding.wordId.text.toString(),
//                    translation = binding.translationId.text.toString(),
//                    example = binding.examplesId.text.toString()
//                )
//            )
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
