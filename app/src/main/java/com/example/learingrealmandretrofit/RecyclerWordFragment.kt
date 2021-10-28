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
import com.example.learingrealmandretrofit.api2.Api

import com.example.learingrealmandretrofit.databinding.FragmentCardMainRecyclerBinding
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


open class RecyclerWordFragment() : Fragment(R.layout.fragment_card_main_recycler) {
    private val adapterRecycler = CardRecyclerAdapter(this)
    private lateinit var binding: FragmentCardMainRecyclerBinding
    private val listWitchObjectsRealmCard: MutableList<RealmCard> = mutableListOf()
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
        deleteAllCardsRealm()
        getAllCardRetrofit()

        Log.d("KEKAS", "$listWitchObjectsRealmCard")
        /*adapterRecycler.setWords(listWitchObjectsRealmCard)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapterRecycler*/



        /*val repository = Repository()
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewModel::class.java)
        viewModel.getPost()
        viewModel.myResponse.observe(viewLifecycleOwner, { response ->
            Log.d("RETROFIT11", "id : ${response.cards}")
        })*/



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
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            DialogSaveWord.newWordKey
        )?.observe(
            viewLifecycleOwner
        ){
            initRealmObjectCard()
            Log.d("KEK1", "New word: $listWitchObjectsRealmCard")
        }
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
                val currentSwipe = wordWithAlphabetHeaders[viewHolder.absoluteAdapterPosition] as RealmCard
                findNavController().navigate(R.id.action_recyclerWordFragment_to_deleteWordFragment,
                    bundleOf(DeleteWordFragment.deleteWordKey to currentSwipe))
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }


   private fun getAllCardRetrofit() {
       val retrofitData = BaseApi.retrofit.getData()
        retrofitData.enqueue(object : Callback<DataServer?> {
            override fun onResponse(call: Call<DataServer?>, response: Response<DataServer?>) {
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
            override fun onFailure(call: Call<DataServer?>, t: Throwable) {
            }
        })
    }

    private fun creteCardsRealm(arrayCards: MutableList<RealmCard>) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            for (item in arrayCards) {
                Log.d("KEK123", "Realm : ${item}")
                realmTransaction.insert(item)
            }
        }, {
            pullCardsRealm()
        }, {

        })
    }
    private fun pullCardsRealm() {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            listWitchObjectsRealmCard.clear()
            listWitchObjectsRealmCard.addAll(realmTransaction
                .where(RealmCard::class.java)
                .findAll()
                .map {
                    RealmCard(
                        id = it.id,
                        word = it.word,
                        translation = it.translation,
                        example = it.example
                    )
                }
            )
        }, {
            adapterRecycler.setWords(listWitchObjectsRealmCard)
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapterRecycler
        }, {

        })
    }

    fun parsingResponse(responseBody: DataServer?) {
        val arrayCards = mutableListOf<RealmCard>()
        responseBody?.cards?.forEach{
            arrayCards.add(it)
        }
        creteCardsRealm(arrayCards)
    }

    private fun getMyData(word: String) {
        val retrofitData = BaseApi.retrofit.getData()
        retrofitData.enqueue(object : Callback<DataServer?> {
            override fun onResponse(call: Call<DataServer?>, response: Response<DataServer?>) {
                val responseBody = response.body()!!
                for(myData in responseBody.cards) {
                    if(myData.word == word) {
                        Log.d("KEK1", "Current word : $myData, seach word : $word")
                    }
                }
            }

            override fun onFailure(call: Call<DataServer?>, t: Throwable) {
                Log.d("KEKA1", "Error $t")
            }
        })
        /*fun gorov() {
        Api.getCards(::onGetCardsSuccess, ::onGetCardsFailure)

        Api.getWord("id", ::onGetCardsSuccess, ::onGetCardsFailure)
        }*/
    }
    private fun deleteData(id: Int) {
        val retrofitData = BaseApi.retrofit.deleteData(id)
        retrofitData.enqueue(object : Callback<DataServer?> {
            override fun onResponse(call: Call<DataServer?>, response: Response<DataServer?>) {
                Log.d("KEKA1", "Response ok, id delete : ${response}")
            }
            override fun onFailure(call: Call<DataServer?>, t: Throwable) {
                Log.d("KEKA1", "Error $t")
            }
        })
    }

    private fun updateData(id: Int, word: String, translation: String, example: String) {
        val card = RealmCard(id = id, word = word, translation = translation, example = example)
        val retrofitData = BaseApi.retrofit.updateData(id = id, params = card)
        retrofitData.enqueue(object : Callback<DataServer?> {
            override fun onResponse(call: Call<DataServer?>, response: Response<DataServer?>) {
                Log.d("KEKA1", "Patch ok, $response")
            }
            override fun onFailure(call: Call<DataServer?>, t: Throwable) {
                Log.d("KEK1", "Error patch $t")
            }
        })
    }

    private fun createData(id: Int, word: String, translation: String, example: String) {
        val card = RealmCard(id = id, word = word, translation = translation, example = example)
        val retrofitData = BaseApi.retrofit.createData(card)
        retrofitData.enqueue(object : Callback<DataServer?> {
            override fun onResponse(call: Call<DataServer?>, response: Response<DataServer?>) {
                Log.d("KEKA1", "Create new data ok : $response")
            }

            override fun onFailure(call: Call<DataServer?>, t: Throwable) {
                Log.d("KEKA1", "Create new failure : $t")
            }
        })
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
    fun onItemClick(cardRealm: RealmCard) {
        findNavController().navigate(R.id.action_recyclerWordFragment_to_dialogSaveWord,
        bundleOf(DialogSaveWord.cardRealmKey to cardRealm))

    }
    private fun initRealmObjectCard(){
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync( {realmTransaction ->
            listWitchObjectsRealmCard.clear()
            listWitchObjectsRealmCard.addAll(realmTransaction
                .where(RealmCard::class.java)
                .findAll()
                .map {
                    RealmCard(
                        id = it.id,
                        word = it.word,
                        translation = it.translation,
                        example = it.example
                    )
                }
            )
        }, {
            adapterRecycler.setWords(listWitchObjectsRealmCard)
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapterRecycler
            Log.v("EXAMPLE", "Successfully completed the transaction")
        }, { error ->
            Log.e("EXAMPLE", "Failed the transaction: $error")
        })
    }
    fun buildGeneralArray(wordsList: MutableList<RealmCard>): ArrayList<Any> {
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

    private fun onGetCardsSuccess(response: DataServer?) {
        Log.d("KEK", "onGetCardsSuccess: ${response?.cards}")
    }

    private fun ord(response: String?) {
        Log.d("KEK", "onGetCardsSuccess: ${response}")
    }

    private fun onGetCardsFailure(error: Throwable) {
        Log.d("KEK", "onGetCardsFailure: $error")
    }
    private fun deleteAllCardsRealm() {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransaction { realmTransaction ->
            val result = realmTransaction
                .where(RealmCard::class.java)
                .findAll()
            result.deleteAllFromRealm()
        }
    }
    private fun updateCardsRealm() {

    }



    private fun deleteCardRealm(id: Int) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransaction { realmTransaction ->
            val result = realmTransaction
                .where(RealmCard::class.java)
                .equalTo("id", id)
                .findAll()
            result.deleteAllFromRealm()
        }
    }
    private fun readAllCard() : RealmResults<RealmCard> {
        val realm = Realm.getDefaultInstance()
        return realm.where(RealmCard::class.java).findAll().sort("word", Sort.ASCENDING)
    }
}
