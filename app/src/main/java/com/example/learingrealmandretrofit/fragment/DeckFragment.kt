package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DeckFragmentRecyclerBinding
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.DeckParameters
import com.example.learingrealmandretrofit.objects.Deck
import com.example.learingrealmandretrofit.objects.response.DeckListResponse
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeckFragment : Fragment() {
    private lateinit var binding: DeckFragmentRecyclerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeckFragmentRecyclerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        deleteAllDecksRealm()
        getAllDecksRetrofit()

        binding.buttonCreateDeck.setOnClickListener {
            findNavController().navigate(R.id.action_deckFragment_to_dialogCreateOrChangeDeck)
        }
        val item = object : SwipeToDeleteCard(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                findNavController().navigate(DeckFragmentDirections.actionDeckFragmentToDialogDeleteDeck())
                binding.recyclerDeck.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.recyclerDeck)
    }

    private fun getAllDecksRetrofit(){
        BaseApi.retrofit.getDeck().enqueue(object : Callback<DeckListResponse?> {
            override fun onResponse(
                call: Call<DeckListResponse?>,
                response: Response<DeckListResponse?>
            ) {
                val responseBody = response.body()?.decks
                val statusCode = response.code()
                if (response.isSuccessful && responseBody != null) {
                    createDecksRealm(responseBody)
                } else {
                    context?.showErrorCodeToast(statusCode)
                }
            }
            override fun onFailure(call: Call<DeckListResponse?>, t: Throwable) {
                context?.showErrorToast(R.string.connection_issues)
            }
        })
    }

    private fun createDecksRealm(arrayDeck: List<DeckParameters>) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            for (item in arrayDeck) {
                val deckRealm = Deck(
                    id = item.id,
                    title = item.title
                )
                realmTransaction.insert(deckRealm)
            }
        }, {
            val deckResult = pullDecksRealm()
            val adapter = RecyclerAdapterDeck(this, deckResult, true)
            binding.recyclerDeck.layoutManager = LinearLayoutManager(context)
            binding.recyclerDeck.adapter = adapter
            realm.close()
        }, {
            Log.d("ERROR1", "${it.message}")
            realm.close()
        })
    }

    private fun pullDecksRealm() : RealmResults<Deck> {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        return realm.where(Deck::class.java).findAll()
    }

    fun onItemClick(deck: Deck) {
        val list = listOf<CardParameters>()
        val deck = DeckParameters(deck.id,deck.title, list)
        val direction = DeckFragmentDirections.actionDeckFragmentToInsideDeckCardFragment(
            deck = deck
        )
        findNavController().navigate(direction)
    }

    private fun deleteAllDecksRealm() {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            val result = realmTransaction
                .where(Deck::class.java)
                .findAll()
            result.deleteAllFromRealm()
        }, {
            realm.close()
        }, {
            realm.close()
        })
    }
}
