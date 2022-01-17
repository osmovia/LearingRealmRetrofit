package com.example.learingrealmandretrofit.card

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.viewmodel.CardViewModel
import com.example.learingrealmandretrofit.card.viewmodel.factory.CardViewModelFactory
import com.example.learingrealmandretrofit.databinding.CardFragmentRecyclerBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class CardFragment : CardActionsFragment() {

    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Ads google"


    private lateinit var binding: CardFragmentRecyclerBinding
    private lateinit var viewModelFactory: CardViewModelFactory
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

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }

        if (mInterstitialAd != null) {
            mInterstitialAd?.show(requireActivity())
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.")
        }

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.deckFragment, R.id.cardFragment, R.id.settingFragment))

        NavigationUI.setupWithNavController(binding.toolbarContainerCard.toolbarId, findNavController(), appBarConfiguration)

        viewModelFactory = CardViewModelFactory(application = requireActivity().application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CardViewModel::class.java)

        binding.adView.loadAd(AdRequest.Builder().build())

        viewModel.pullCardsRealm()
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
            val action = CardFragmentDirections.actionCardFragmentToDialogCreateOrChangeCard(
                card = null,
            )
            findNavController().navigate(action)
        }

        val itemDelete = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipeCardId = viewModel.getAllCardsRealm.value?.get(viewHolder.absoluteAdapterPosition)?.id
                if (currentSwipeCardId != null) {
                    val action = CardFragmentDirections.actionCardFragmentToDialogDeleteCard(
                        cardId = currentSwipeCardId,
                    )
                    findNavController().navigate(action)
                    binding.recyclerCard.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
            }
        }
        val itemTouchHelperDelete = ItemTouchHelper(itemDelete)
        itemTouchHelperDelete.attachToRecyclerView(binding.recyclerCard)

        val itemRelationship = object : SwipeToAddRelationship(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipeCardId = viewModel.getAllCardsRealm.value?.get(viewHolder.absoluteAdapterPosition)?.id
                if (currentSwipeCardId != null) {
                    val action = CardFragmentDirections.actionCardFragmentToAddCardToDeckFragment(
                        cardId = currentSwipeCardId,
                    )
                    findNavController().navigate(action)
                    binding.recyclerCard.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
            }
        }
        val itemTouchHelperRelationship = ItemTouchHelper(itemRelationship)
        itemTouchHelperRelationship.attachToRecyclerView(binding.recyclerCard)
    }

    override fun onCardClick(card: Card) {
        val action = CardFragmentDirections.actionCardFragmentToShowDetailsCardFragment(cardId = card.id)
        findNavController().navigate(action)
    }
}
