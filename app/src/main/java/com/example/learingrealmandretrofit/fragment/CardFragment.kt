package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.CardFragmentRecyclerBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.CardRealm
import com.example.learingrealmandretrofit.objects.response.CardListResponse
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardFragment : Fragment() {
    private lateinit var binding: CardFragmentRecyclerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CardFragmentRecyclerBinding.inflate(layoutInflater)
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

        binding.floatingActionButtonCard.setOnClickListener {
            findNavController().navigate(R.id.action_cardFragment_to_dialogCreateOrChangeCard)
        }

        val item = object : SwipeToDeleteCard(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipeCardId = pullCardsRealm()[viewHolder.absoluteAdapterPosition]?.id
                if (currentSwipeCardId != null) {
                    val arrow = CardFragmentDirections.actionCardFragmentToDialogDeleteCard(currentSwipeCardId)
                    findNavController().navigate(arrow)
                    binding.recyclerCard.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.recyclerCard)
    }

   private fun getAllCardRetrofit() {
       val retrofitData = BaseApi.retrofit.getCards()
        retrofitData.enqueue(object : Callback<CardListResponse?> {
            override fun onResponse(call: Call<CardListResponse?>, response: Response<CardListResponse?>) {
                val statusCode = response.code()
                val responseBody = response.body()?.cards
                if(response.isSuccessful && responseBody != null) {
                    when (statusCode) {
                        in 200..299 -> createCardsRealm(responseBody)
                        in 400..499 -> context?.showErrorToast()
                        in 500..600 -> context?.showErrorToast()
                        else -> context?.showErrorCodeToast(statusCode)
                    }
                }
            }
            override fun onFailure(call: Call<CardListResponse?>, t: Throwable) {
                context?.showErrorToast(R.string.connection_issues)
            }
        })
    }

    private fun createCardsRealm(arrayCards: List<Card>) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            for (item in arrayCards) {
                val cardRealm = CardRealm(
                    id = item.id,
                    word = item.word,
                    translation = item.translation,
                    example = item.example
                )
                realmTransaction.insert(cardRealm)
            }
        }, {
            val cardResult = pullCardsRealm()
            val adapter = RecyclerAdapterCard(this, cardResult, true)
            binding.recyclerCard.layoutManager = LinearLayoutManager(context)
            binding.recyclerCard.adapter = adapter
            realm.close()
        }, {
            realm.close()
        })
    }

    private fun pullCardsRealm() : RealmResults<CardRealm> {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        return  realm.where(CardRealm::class.java).findAll().sort("word", Sort.ASCENDING)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    fun onItemClick(cardRealm: CardRealm) {
        findNavController().navigate(
            R.id.action_cardFragment_to_dialogCreateOrChangeCard,
        bundleOf(DialogCreateOrChangeCard.cardRealmKey to cardRealm))
    }

    private fun deleteAllCardsRealm() {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            val result = realmTransaction
                .where(CardRealm::class.java)
                .findAll()
            result.deleteAllFromRealm()
        }, {
            realm.close()
        }, {
            realm.close()
        })
    }
}
