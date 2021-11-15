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
import com.example.learingrealmandretrofit.objects.CardRealm
import com.example.learingrealmandretrofit.objects.response.CardListResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.Sort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardFragment : Fragment() {
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
        testing()
        Realm.init(context)

        val config = RealmConfiguration.Builder().build()
        Realm.deleteRealm(config)

        deleteAllCards()
        getAllCardRetrofit()

        binding.buttonFloatingAction.setOnClickListener {
            findNavController().navigate(R.id.action_recyclerCardFragment_to_dialogSaveOrChangeCard)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            DeleteCardFragment.noDeleteWordKey
        )?.observe(
            viewLifecycleOwner
        ) {
            binding.recyclerCard.adapter?.notifyItemChanged(currentSwipePosition)
        }

        val item = object : SwipeToDeleteCard(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                currentSwipePosition = viewHolder.absoluteAdapterPosition
                val currentSwipeCardId = pullCardsRealm()[viewHolder.absoluteAdapterPosition]?.id
                findNavController().navigate(
                    R.id.action_recyclerCardFragment_to_deleteCardFragment,
                    bundleOf(DeleteCardFragment.deleteWordKey to currentSwipeCardId))
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.recyclerCard)
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
                    in 400..499 -> context?.showErrorToast()
                    in 500..600 -> context?.showErrorToast()
                    else -> Toast.makeText(requireContext(),
                        getString(R.string.code_error_message, statusCode),
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
                val cardRealm = CardRealm(
                    id = item.id,
                    word = item.word,
                    translation = item.translation,
                    example = item.example
                )
                realmTransaction.insert(cardRealm)
            }
        }, {
            val cardResult = pullCardsRealm()
            val adapter = RecyclerAdapterCard(this, cardResult, true)
            binding.recyclerCard.layoutManager = LinearLayoutManager(context)
            binding.recyclerCard.adapter = adapter
            realm.close()
        }, {
            realm.close()
        })
    }
    private fun pullCardsRealm() : RealmResults<CardRealm> {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        return  realm.where(CardRealm::class.java).findAll().sort("word", Sort.ASCENDING)
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
        findNavController().navigate(R.id.action_recyclerCardFragment_to_registrationFragment)
        return true
    }

    fun onItemClick(cardRealm: CardRealm) {
        findNavController().navigate(
            R.id.action_recyclerCardFragment_to_dialogSaveOrChangeCard,
        bundleOf(DialogCreateOrChangeCard.cardRealmKey to cardRealm))
    }

    private fun deleteAllCards() {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            val result = realmTransaction
                .where(CardRealm::class.java)
                .findAll()
            result.deleteAllFromRealm()
        }, {
            realm.close()
        }, {
            realm.close()
        })
    }

    fun testing() {
        val gson = Gson()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()

        val tut = Card(39, "word", "example", "translation")

        val jsonTut: String = gson.toJson(tut)
        Log.d("TESTING", jsonTut)

        val jsonTutPretty: String = gsonPretty.toJson(tut)
        Log.d("TESTING", jsonTutPretty)

    }
}
