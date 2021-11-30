package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DialogDeleteCardBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.response.CardResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DialogDeleteCard : DialogFragment() {
    //Arguments
    private val args: DialogDeleteCardArgs by navArgs()
    // Bindings
    private lateinit var binding: DialogDeleteCardBinding
    // Lifecycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteCardBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonYes.setOnClickListener{
            removeCard()
        }
        binding.buttonNo.setOnClickListener{
            findNavController().popBackStack()
        }
    }
    // Functions
    private fun removeCard() {
        val token = context?.user()?.token ?: ""
        requireActivity().showProgress()
        BaseApi.retrofit.deleteCard(id = args.idCard, token = token).enqueue(object : Callback<CardResponse?> {
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val responseBody = response.body()
                val statusCode = response.code()
                if (response.isSuccessful && responseBody != null) {
                    requireActivity().hideProgress()
                    removeFromRealm(responseBody.card.id)
                } else {
                    requireActivity().hideProgress()
                    findNavController().popBackStack()
                    context?.showErrorCodeToast(statusCode)
                }
            }
            override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                requireActivity().hideProgress()
                findNavController().popBackStack()
                context?.showErrorToast()
            }
        })
    }

    private fun removeFromRealm(id: Int) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val result = realmTransaction
                .where(Card::class.java)
                .equalTo("id", id)
                .findFirst()
            result?.deleteFromRealm()
        }, {
            findNavController().popBackStack()
            realm.close()
        },{
            realm.close()
        })
    }
}
