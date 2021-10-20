package com.example.learingrealmandretrofit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.learingrealmandretrofit.databinding.FragmentDeleteWordBinding
import io.realm.Realm
import io.realm.RealmConfiguration


class DeleteWordFragment : DialogFragment() {
    companion object {
        const val deleteWordKey = "DELETE_WORD_KEY"
    }
    lateinit var binding: FragmentDeleteWordBinding
    val deleteObject : RealmCard
        get() = arguments?.getSerializable(deleteWordKey) as RealmCard

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteWordBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonYes.setOnClickListener{
            removeFromRealm()
            findNavController().previousBackStackEntry?.savedStateHandle?.set(deleteWordKey, "")
            findNavController().popBackStack()
        }
    }
    private fun removeFromRealm() {
        val config = RealmConfiguration
            .Builder()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        val realm = Realm.getInstance(config)
        realm.executeTransaction { realmTransaction ->
            val result = realmTransaction
                .where(RealmCard::class.java)
                .equalTo("id", deleteObject.id)
                .findAll()
            result.deleteAllFromRealm()
        }
    }
}
