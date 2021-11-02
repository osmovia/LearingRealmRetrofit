package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.api.BaseApi

import com.example.learingrealmandretrofit.databinding.FragmentCardMainRecyclerBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.response.CardListResponse
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class MainFragment : Fragment(R.layout.fragment_card_main_recycler) {
    private lateinit var binding: FragmentCardMainRecyclerBinding
    private var currentSwipePosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardMainRecyclerBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        Realm.init(context)
        getAllCardRetrofit()

        binding.buttonFloatingAction.setOnClickListener {
            findNavController().navigate(R.id.action_recyclerWordFragment_to_dialogSaveWord)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            DeleteWordFragment.noDeleteWordKey
        )?.observe(
            viewLifecycleOwner
        ) {
            binding.recyclerView.adapter?.notifyItemChanged(currentSwipePosition)
        }

        val item = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                currentSwipePosition = viewHolder.absoluteAdapterPosition
                val currentSwipeCard = pullCardsRealm()[viewHolder.absoluteAdapterPosition]
                findNavController().navigate(
                    R.id.action_recyclerWordFragment_to_deleteWordFragment,
                    bundleOf(DeleteWordFragment.deleteWordKey to currentSwipeCard))
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onDestroy() {
        Log.d("Destroy", "Destroy Main")
        deleteAllCards()
        super.onDestroy()
    }

   private fun getAllCardRetrofit() {
       val retrofitData = BaseApi.retrofit.getCards()
        retrofitData.enqueue(object : Callback<CardListResponse?> {
            override fun onResponse(call: Call<CardListResponse?>, response: Response<CardListResponse?>) {
                val statusCode = response.code()
                val responseBody = response.body()
                response.isSuccessful
                when(statusCode) {
                    in 200..299 -> parsingResponse(responseBody)
                    in 400..499 -> Toast.makeText(requireContext(),
                            "Please check your internet connection",
                            Toast.LENGTH_LONG).show()
                    in 500..600 -> Toast.makeText(requireContext(),
                        "Oops problem on the server, try again later",
                        Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(requireContext(),
                        "Code error : $statusCode",
                        Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<CardListResponse?>, t: Throwable) {
                Toast.makeText(requireContext(), "Oops an error occurred, check connect internet.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun creteCardsRealm(arrayCards: MutableList<Card>) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            for (item in arrayCards) {
                realmTransaction.insert(item)
            }
        }, {
            val cardResult = pullCardsRealm()
            val adapter = RealmRecyclerAdapter(this, cardResult, true)
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapter
            realm.close()
        }, {
            realm.close()
        })
    }
    private fun pullCardsRealm() : RealmResults<Card> {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        return  realm.where(Card::class.java).findAll().sort("word", Sort.ASCENDING)
    }

    fun parsingResponse(responseBody: CardListResponse?) {
        val arrayCards = mutableListOf<Card>()
        responseBody?.cards?.forEach{
            arrayCards.add(it)
        }
        creteCardsRealm(arrayCards)
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    fun onItemClick(cardRealm: Card) {
        findNavController().navigate(
            R.id.action_recyclerWordFragment_to_dialogSaveWord,
        bundleOf(DialogSaveWord.cardRealmKey to cardRealm))
    }

    private fun deleteAllCards() {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            val result = realmTransaction
                .where(Card::class.java)
                .findAll()
            result.deleteAllFromRealm()
        }, {
            realm.close()
        }, {
            realm.close()
        })
    }
}
