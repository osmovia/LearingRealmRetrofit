package com.example.learingrealmandretrofit

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.learingrealmandretrofit.databinding.FragmentCardMainRecyclerBinding
import io.realm.Realm
import io.realm.RealmConfiguration


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
        initRealmObjectCard()
        adapterRecycler.setWords(listWitchObjectsRealmCard)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapterRecycler

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
            adapterRecycler.setWords(listWitchObjectsRealmCard)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            DialogSaveWord.newWordKey
        )?.observe(
            viewLifecycleOwner
        ){
            initRealmObjectCard()
            Log.d("KEK1", "New word: $listWitchObjectsRealmCard")
            adapterRecycler.setWords(listWitchObjectsRealmCard)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            DialogSaveWord.changeWordKey
        )?.observe(
            viewLifecycleOwner
        ){
            initRealmObjectCard()
            Log.d("KEK1", "Change word: $listWitchObjectsRealmCard")
            adapterRecycler.setWords(listWitchObjectsRealmCard)
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
        val config = RealmConfiguration
            .Builder()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
        val realm = Realm.getInstance(config)
        realm.executeTransaction { realmTransaction ->
            listWitchObjectsRealmCard.clear()
            listWitchObjectsRealmCard.addAll(realmTransaction
                .where(RealmCard::class.java)
                .findAll()
                .map {
                    RealmCard(
                        id = it.id,
                        word = it.word,
                        translate = it.translate,
                        example = it.example
                    )
                }
            )
        }
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
}
