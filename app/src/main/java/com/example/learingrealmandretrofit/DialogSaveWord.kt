package com.example.learingrealmandretrofit


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

import com.example.learingrealmandretrofit.databinding.FragmentSaveWordBinding
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait

class DialogSaveWord : DialogFragment() {
    companion object{
        const val newWordKey = "NEW_WORD_KEY"
        const val changeWordKey = "CHANGE_WORD_KEY"
        const val cardRealmKey = "CARD_REALM_KEY"
    }
    private val cardRealm: RealmCard?
        get() = arguments?.getSerializable(cardRealmKey) as? RealmCard?
    lateinit var binding: FragmentSaveWordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveWordBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fillFields()
        binding.saveOrUpdateWord.setOnClickListener{
            if (cardRealm == null) {
                createNewCard()
            } else {
                updateCurrentCard()
            }
        }
    }

    private fun saveCardRealm(card: RealmCard) {
        val config = RealmConfiguration
            .Builder()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        val realm = Realm.getInstance(config)

        realm.executeTransactionAsync({ realmTransaction ->
            realmTransaction.insert(card)
            Log.d("KEK", "Create card realm")
        }, {
            realm.close()
            Log.d("KEK", "Create new card 1")
            findNavController().previousBackStackEntry?.savedStateHandle?.set(newWordKey,"")
            findNavController().popBackStack()
            Log.d("KEK", "Create new card 2")
           }, { realm.close() })

//        1. Init Realm
//        2. Create or update card
//        3. Check result, if ok - hide this fragment and show list of Cards to user.
    }
    private fun createNewCard() {
        val word = binding.editTextOriginalWord.text.toString()
        val translate = binding.editTextTranslateWord.text.toString()
        val example = binding.editTextExample.text.toString()
        val realmCard = RealmCard(word = word, translate = translate, example = example)
        saveCardRealm(realmCard)
    }
    private fun updateCurrentCard() {
        val word = binding.editTextOriginalWord.text.toString()
        val translate = binding.editTextTranslateWord.text.toString()
        val example = binding.editTextExample.text.toString()
        val id = cardRealm?.id.toString()
        val cardRealm = RealmCard(word = word, translate = translate, example = example ,id = id)
        updateCardRealm(cardRealm)
        Log.d("KEK", "Update current card 1")
        findNavController().previousBackStackEntry?.savedStateHandle?.set(changeWordKey,"")
        findNavController().popBackStack()
        Log.d("KEK", "Update current card 2")

    }
    private fun updateCardRealm(cardRealm : RealmCard) {
        val config = RealmConfiguration
            .Builder()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        val realm = Realm.getInstance(config)
        realm.executeTransaction { realmTransaction ->
            val result = realmTransaction
                .where(RealmCard::class.java)
                .equalTo("id", cardRealm.id)
                .findFirst()
            result?.example = cardRealm.example
            result?.translate = cardRealm.translate
            result?.word = cardRealm.word
            Log.d("KEK", "Update card realm")
        }
    }
    private fun fillFields() {
        if(cardRealm != null){
            binding.editTextOriginalWord.setText(cardRealm?.word.toString())
            binding.editTextTranslateWord.setText(cardRealm?.translate.toString())
            binding.editTextExample.setText(cardRealm?.example.toString())
        }
    }
}


