package com.example.learingrealmandretrofit.api2

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.learingrealmandretrofit.DataServer
import com.example.learingrealmandretrofit.api.SimpleApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Path

object Api {

    /*private const val baseUrl: String = "http://10.0.1.83:3000"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IRequests::class.java)
    }
    fun getCards(
        onSuccess: (DataServer?, Int?) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        retrofit.getCards()
            .enqueue(CommonCallbackImplementation(onSuccess, onFailure))
    }
    fun deleteCard(
        id : Int,
        onSuccess: (DataServer?, Int?) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        retrofit.deleteData(id)
            .enqueue(CommonCallbackImplementation(onSuccess, onFailure))
    }

    *//*fun getWord(
        id: String,
        onSuccess: (String?, Int?) -> Unit,
        onFailure: (throwable: Throwable) -> Unit,
    ) {
        retrofit.getWord(id)
            .enqueue(CommonCallbackImplementation(onSuccess, onFailure))
    }*//*


    class CommonCallbackImplementation<T>(

        private val onSuccess: (Call<T>?, Response<T>?) -> Unit,
        private val onFailure: ((Throwable) -> Unit)? = null,
    ) : Callback<T> {

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailure?.invoke(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                val statusCode = response.code()
                onSuccess(response.body(), response.code())
                response.isSuccessful
                when(statusCode) {
                    in 200..299 -> parsingResponse(responseBody)
                    in 400..499 -> Log.d("Response ")
                    in 500..600 -> Toast.makeText(requireContext(),
                        "Oops problem on the server, try again later",
                        Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(context,
                        "Code error : $statusCode",
                        Toast.LENGTH_LONG).show()
                }
            } else {
                try {
                    val message =
                        JSONObject(response.errorBody()?.string() ?: "").getString("message")
                    onFailure?.invoke(Throwable(message = message))
                } catch (e: Exception) {
                    onFailure?.invoke(Throwable(message = "Request failure"))
                }
            }
        }
    }*/
}