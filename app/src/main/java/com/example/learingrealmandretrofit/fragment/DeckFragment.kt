package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learingrealmandretrofit.ConfigRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.RecyclerAdapterDeck
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DeckFragmentRecyclerBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.Deck
import com.example.learingrealmandretrofit.objects.DeckRealm
import com.example.learingrealmandretrofit.objects.response.DeckListResponse
import com.example.learingrealmandretrofit.showErrorToast
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

        Realm.init(context)
        deleteAllDecksRealm()
        getAllDecksRetrofit()

        binding.buttonFloatingAction.setOnClickListener {
            findNavController().navigate(R.id.action_deckFragment_to_dialogCreateOrChangeDeck)
        }
    }

    private fun getAllDecksRetrofit(){
        BaseApi.retrofit.getDeck().enqueue(object : Callback<DeckListResponse?> {
            override fun onResponse(
                call: Call<DeckListResponse?>,
                response: Response<DeckListResponse?>
            ) {
                Log.d("KEK", "Response : ${response.body()}")
                if (response.isSuccessful) {
                    Log.d("KEK", "isSuccessful")
                    val responseBody = response.body()?.decks!!
                    createDecksRealm(responseBody)
                } else {
                    Log.d("KEK","isSuccessful error")
                }

            }
            override fun onFailure(call: Call<DeckListResponse?>, t: Throwable) {
                context?.showErrorToast(R.string.error_server)
            }
        })
    }


    private fun createDecksRealm(arrayDeck: List<Deck>) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            for (item in arrayDeck) {
                val deckRealm = DeckRealm(
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

    private fun pullDecksRealm() : RealmResults<DeckRealm> {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        return realm.where(DeckRealm::class.java).findAll()
    }

    fun onItemClick(deckRealm: DeckRealm) {
//        val list = listOf<Card>()
//        val deck = Deck(1,"", list)
//        val direction = DeckFragmentDirections.actionDeckFragmentToInsideDeckCardFragment(
//            deck = deck
//        )
//        findNavController().navigate(direction)
        findNavController().navigate(R.id.action_deckFragment_to_insideDeckCardFragment)
    }

    private fun deleteAllDecksRealm() {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            val result = realmTransaction
                .where(DeckRealm::class.java)
                .findAll()
            result.deleteAllFromRealm()
        }, {
            realm.close()
        }, {
            realm.close()
        })
    }
}
