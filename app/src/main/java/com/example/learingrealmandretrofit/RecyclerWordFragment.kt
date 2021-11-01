package com.example.learingrealmandretrofit

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
import com.example.learingrealmandretrofit.api.BaseApi

import com.example.learingrealmandretrofit.databinding.FragmentCardMainRecyclerBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.response.CardListResponse
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class RecyclerWordFragment : Fragment(R.layout.fragment_card_main_recycler) {
    private lateinit var binding: FragmentCardMainRecyclerBinding
    private val listWitchObjectsRealmCard: MutableList<Card> = mutableListOf()
    private val wordWithAlphabetHeaders: ArrayList<Any> = arrayListOf()

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
        deleteAllCards()
        getAllCardRetrofit()

        Log.d("KEKAS", "$listWitchObjectsRealmCard")




        binding.buttonFloatingAction.setOnClickListener {
            findNavController().navigate(R.id.action_recyclerWordFragment_to_dialogSaveWord)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            DeleteWordFragment.deleteWordKey
        )?.observe(
            viewLifecycleOwner
        ) {
            initRealmObjectCard()
            Log.d("KEK1", "Delete word: $listWitchObjectsRealmCard")
        }
//
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            DialogSaveWord.changeWordKey
        )?.observe(
            viewLifecycleOwner
        ){
            initRealmObjectCard()
            Log.d("KEK1", "Change word: $listWitchObjectsRealmCard")
        }

        val item = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipe = wordWithAlphabetHeaders[viewHolder.absoluteAdapterPosition] as Card
                findNavController().navigate(R.id.action_recyclerWordFragment_to_deleteWordFragment,
                    bundleOf(DeleteWordFragment.deleteWordKey to currentSwipe))
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
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
            val adapter = RealmRecyclerAdapter(this, context, cardResult, true)
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapter
        }, {

        })
    }
    private fun pullCardsRealm() : RealmResults<Card> {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        return  realm.where(Card::class.java).findAll()
    }

    fun parsingResponse(responseBody: CardListResponse?) {
        val arrayCards = mutableListOf<Card>()
        responseBody?.cards?.forEach{
            arrayCards.add(it)
        }
        creteCardsRealm(arrayCards)
    }

    private fun getMyData(word: String) {
        val retrofitData = BaseApi.retrofit.getCards()
        retrofitData.enqueue(object : Callback<CardListResponse?> {
            override fun onResponse(call: Call<CardListResponse?>, response: Response<CardListResponse?>) {
                val responseBody = response.body()!!
                for(myData in responseBody.cards) {
                    if(myData.word == word) {
                        Log.d("KEK1", "Current word : $myData, seach word : $word")
                    }
                }
            }

            override fun onFailure(call: Call<CardListResponse?>, t: Throwable) {
                Log.d("KEKA1", "Error $t")
            }
        })

    }
    private fun deleteData(id: Int) {
        val retrofitData = BaseApi.retrofit.deleteCard(id)
        retrofitData.enqueue(object : Callback<CardListResponse?> {
            override fun onResponse(call: Call<CardListResponse?>, response: Response<CardListResponse?>) {
                Log.d("KEKA1", "Response ok, id delete : ${response}")
            }
            override fun onFailure(call: Call<CardListResponse?>, t: Throwable) {
                Log.d("KEKA1", "Error $t")
            }
        })
    }

    private fun updateData(id: Int, word: String, translation: String, example: String) {
        val card = Card(id = id, word = word, translation = translation, example = example)
        val retrofitData = BaseApi.retrofit.updateCard(id = id, params = card)
        retrofitData.enqueue(object : Callback<CardListResponse?> {
            override fun onResponse(call: Call<CardListResponse?>, response: Response<CardListResponse?>) {
                Log.d("KEKA1", "Patch ok, $response")
            }
            override fun onFailure(call: Call<CardListResponse?>, t: Throwable) {
                Log.d("KEK1", "Error patch $t")
            }
        })
    }

//    private fun createData(id: Int, word: String, translation: String, example: String) {
//        val card = Card(id = id, word = word, translation = translation, example = example)
//        val retrofitData = BaseApi.retrofit.createCard(card)
//        retrofitData.enqueue(object : Callback<DataServer?> {
//            override fun onResponse(call: Call<DataServer?>, response: Response<DataServer?>) {
//                Log.d("KEKA1", "Create new data ok : $response")
//            }
//
//            override fun onFailure(call: Call<DataServer?>, t: Throwable) {
//                Log.d("KEKA1", "Create new failure : $t")
//            }
//        })
//    }

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
        findNavController().navigate(R.id.action_recyclerWordFragment_to_dialogSaveWord,
        bundleOf(DialogSaveWord.cardRealmKey to cardRealm))

    }
    private fun initRealmObjectCard(){
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync( {realmTransaction ->
            listWitchObjectsRealmCard.clear()
            listWitchObjectsRealmCard.addAll(realmTransaction
                .where(Card::class.java)
                .findAll()
                .map {
                    Card(
                        id = it.id,
                        word = it.word,
                        translation = it.translation,
                        example = it.example
                    )
                }
            )
        }, {
            Log.v("EXAMPLE", "Successfully completed the transaction")
        }, { error ->
            Log.e("EXAMPLE", "Failed the transaction: $error")
        })
    }
    fun buildGeneralArray(wordsList: MutableList<Card>): ArrayList<Any> {
        wordWithAlphabetHeaders.clear()
        wordsList.sortBy {
            it.word
        }
        var currentHeader: String? = null
        wordsList.forEach { cardObject ->
            cardObject.word?.firstOrNull()?.toString()?.let { firstCharacter ->
                if (firstCharacter != currentHeader) {
                    wordWithAlphabetHeaders.add(firstCharacter)
                    currentHeader = firstCharacter
                }
            }
            wordWithAlphabetHeaders.add(cardObject)
        }
        return wordWithAlphabetHeaders
    }

    private fun onGetCardsSuccess(response: CardListResponse?) {
        Log.d("KEK", "onGetCardsSuccess: ${response?.cards}")
    }

    private fun ord(response: String?) {
        Log.d("KEK", "onGetCardsSuccess: ${response}")
    }

    private fun onGetCardsFailure(error: Throwable) {
        Log.d("KEK", "onGetCardsFailure: $error")
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
            Log.d("DeleteRealm", "Delete all card ok.")
            realm.close()
        }, {
            Log.d("DeleteRealm", "Delete all card error.")
        })
    }
    private fun updateCardsRealm() {

    }



    private fun deleteCardRealm(id: Int) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransaction { realmTransaction ->
            val result = realmTransaction
                .where(Card::class.java)
                .equalTo("id", id)
                .findAll()
            result.deleteAllFromRealm()
        }
    }
}
