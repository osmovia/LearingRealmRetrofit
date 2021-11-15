package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
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
    companion object {
        const val noDeleteWordKey = "NO_DELETE_WORD_KEY"
        const val deleteWordKey = "DELETE_WORD_KEY"

    }
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
            noDeleteCard()
        }
    }
    // Functions
    private fun removeCard() {
        arguments?.getInt(deleteWordKey)?.let {
            BaseApi.retrofit.deleteCard(it).enqueue(object : Callback<Success?> {
                override fun onResponse(call: Call<Success?>, response: Response<Success?>) {
                    if (response.isSuccessful && response.body() != null) {
                        if(response.body()?.success == true) {
                            removeFromRealm(it)
                        } else {
                            noDeleteCard()
                            context?.showErrorToast()
                        }
                    } else {
                        noDeleteCard()
                        context?.showErrorToast(R.string.error_server)
                    }
                }
                override fun onFailure(call: Call<Success?>, t: Throwable) {
                    noDeleteCard()
                    context?.showErrorToast()
                }
            })
        }
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

    private fun noDeleteCard() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(noDeleteWordKey, "")
        findNavController().popBackStack()
    }
}
