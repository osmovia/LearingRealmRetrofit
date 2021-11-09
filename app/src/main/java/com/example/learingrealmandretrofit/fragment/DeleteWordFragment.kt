package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.learingrealmandretrofit.ConfigRealm
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.FragmentDeleteWordBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.response.CardResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeleteWordFragment : DialogFragment() {
    companion object {
        const val noDeleteWordKey = "NO_DELETE_WORD_KEY"
        const val deleteWordKey = "DELETE_WORD_KEY"

    }

    // Bindings
    private lateinit var binding: FragmentDeleteWordBinding
    // Lifecycle
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
            removeCard()
        }
        binding.buttonNo.setOnClickListener{
            noDeleteCard()
        }
    }
    // Functions
    private fun removeCard() {
        val card = arguments?.getSerializable(deleteWordKey) as Card
         BaseApi.retrofit.deleteCard(card.id!!).enqueue(object : Callback<CardResponse?> {
             override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                 if(response.isSuccessful) {
                     val responseBody = response.body()
                     if (responseBody != null) {
                         removeFromRealm(responseBody.card)
                     }
                 } else {
                     noDeleteCard()
                     Toast.makeText(requireContext(), "Oops an error occurred, please try again later.", Toast.LENGTH_LONG).show()
                 }
             }
             override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                 noDeleteCard()
                 Toast.makeText(requireContext(), "Oops an error occurred, check connect internet.", Toast.LENGTH_LONG).show()
             }
         })
    }

    private fun removeFromRealm(card: Card) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val result = realmTransaction
                .where(Card::class.java)
                .equalTo("id", card.id)
                .findFirst()
            result?.deleteFromRealm()
        }, {
            findNavController().popBackStack()
            realm.close()
        },{
            realm.close()
        })
    }

    private fun noDeleteCard() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(noDeleteWordKey, "")
        findNavController().popBackStack()
    }
}
