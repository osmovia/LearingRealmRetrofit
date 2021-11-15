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
import com.example.learingrealmandretrofit.objects.CardRealm
import com.example.learingrealmandretrofit.objects.response.Success
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
        arguments?.getInt(deleteWordKey)?.let {
            BaseApi.retrofit.deleteCard(it).enqueue(object : Callback<Success?> {
                override fun onResponse(call: Call<Success?>, response: Response<Success?>) {
                    if (response.isSuccessful && response.body() != null) {
                        if(response.body()?.success == true) {
                            removeFromRealm(it)
                        } else {
                            noDeleteCard()
                            Toast.makeText(requireContext(), "Oops an error occurred, please try again later.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        noDeleteCard()
                        Toast.makeText(requireContext(), "Oops an error occurred, please try again later.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Success?>, t: Throwable) {
                    noDeleteCard()
                    Toast.makeText(requireContext(), "Oops an error occurred, check connect internet.", Toast.LENGTH_LONG).show()
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
