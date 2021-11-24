package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learingrealmandretrofit.ConfigRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DialogDeleteCardBinding
import com.example.learingrealmandretrofit.objects.CardRealm
import com.example.learingrealmandretrofit.objects.response.Success
import com.example.learingrealmandretrofit.showErrorToast
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
            BaseApi.retrofit.deleteCard(args.idCard).enqueue(object : Callback<Success?> {
                override fun onResponse(call: Call<Success?>, response: Response<Success?>) {
                    if (response.isSuccessful && response.body() != null) {
                        if(response.body()?.success == true) {
                            removeFromRealm(args.idCard)
                        } else {
                            findNavController().popBackStack()
                            context?.showErrorToast()
                        }
                    } else {
                        findNavController().popBackStack()
                        context?.showErrorToast(R.string.connection_issues)
                    }
                }
                override fun onFailure(call: Call<Success?>, t: Throwable) {
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
                .where(CardRealm::class.java)
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
