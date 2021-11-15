package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.ConfigRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.RecyclerAdapterDeck
import com.example.learingrealmandretrofit.SwipeToDeleteCard
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DeckFragmentRecyclerBinding
import com.example.learingrealmandretrofit.objects.Deck
import com.example.learingrealmandretrofit.objects.DeckRealm
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

        binding.post.setOnClickListener {
            getDeck()
        }

//        val item = object : SwipeToDeleteCard(requireContext()) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                currentSwipePosition = viewHolder.absoluteAdapterPosition
//                val currentSwipeCardId = pullCardsRealm()[viewHolder.absoluteAdapterPosition]?.id
//                findNavController().navigate(
//                    R.id.action_recyclerCardFragment_to_deleteCardFragment,
//                    bundleOf(DeleteCardFragment.deleteWordKey to currentSwipeCardId))
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(item)
//        itemTouchHelper.attachToRecyclerView(binding.recyclerDeck)

    }

    fun getDeck(){
        BaseApi.retrofit.getDeck().enqueue(object : Callback<DeckListResponse?> {
            override fun onResponse(
                call: Call<DeckListResponse?>,
                response: Response<DeckListResponse?>
            ) {
                Log.d("KEK", "Response : ${response.body()}")
                if (response.isSuccessful) {
                    Log.d("KEK", "isSuccessful")
                    val responseBody = response.body()?.decks
                    parsingResponse(responseBody)
                } else {
                    Log.d("KEK","isSuccessful error")
                }

            }
            override fun onFailure(call: Call<DeckListResponse?>, t: Throwable) {
                Log.d("KEK", "onFailure ${t.message}")
            }
        })

    }
    fun parsingResponse(responseBody: List<Deck>?) {
        val arrayDeck = mutableListOf<Deck>()
        responseBody?.forEach {
            arrayDeck.add(it)
        }
        createDecksRealm(arrayDeck)
    }

    fun createDecksRealm(arrayDeck: MutableList<Deck>) {
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
            realm.close()
        })
    }

    fun pullDecksRealm() : RealmResults<DeckRealm> {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        return realm.where(DeckRealm::class.java).findAll()

    }

    fun onItemClick(deckRealm: DeckRealm) {
        findNavController().navigate(
            R.id.action_deckFragment_to_dialogCreateOrChangeDeck,
            bundleOf(DialogCreateOrChangeDeck.deckRealmKey to deckRealm)
        )
    }
}