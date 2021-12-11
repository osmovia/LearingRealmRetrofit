package com.example.learingrealmandretrofit.card

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.viewmodel.CardViewModel
import com.example.learingrealmandretrofit.databinding.CardFragmentRecyclerBinding

class CardFragment : Fragment() {

    private lateinit var binding: CardFragmentRecyclerBinding
    private lateinit var viewModel: CardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CardFragmentRecyclerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this).get(CardViewModel::class.java)

        viewModel.token.value = SharedPreferencesManager(requireContext()).fetchAuthentication().sessionToken

        viewModel.getAllCardRetrofit()

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            if (showSpinner) {
                activity?.showProgress()
            } else {
                activity?.hideProgress()
            }
        })

        viewModel.getAllCardsRealm.observe(viewLifecycleOwner, Observer { allCards ->
            val adapter = RecyclerAdapterCard(this, allCards, true)
            binding.recyclerCard.layoutManager = LinearLayoutManager(context)
            binding.recyclerCard.adapter = adapter
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        binding.buttonCreateCard.setOnClickListener {
            val action = CardFragmentDirections.actionCardFragmentToDialogCreateOrChangeCard(null)
             findNavController().navigate(action)
        }

        val item = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipeCardId = viewModel.getAllCardsRealm.value?.get(viewHolder.absoluteAdapterPosition)?.id
                if (currentSwipeCardId != null) {
                    val action = CardFragmentDirections.actionCardFragmentToDialogDeleteCard(currentSwipeCardId)
                    findNavController().navigate(action)
                    binding.recyclerCard.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.recyclerCard)
    }

    fun onItemClick(card: Card) {
        val action = CardFragmentDirections.actionCardFragmentToDialogCreateOrChangeCard(card = card)
        findNavController().navigate(action)
    }
}
