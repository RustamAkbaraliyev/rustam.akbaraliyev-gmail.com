package uz.mypay.android.data.source.local

import android.content.Context
import uz.mypay.android.model.Card
import uz.mypay.android.util.format
import uz.mypay.android.util.toAmount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardDataSource @Inject constructor(context: Context) {
    private val pref = context.getSharedPreferences("CardDataSource", Context.MODE_PRIVATE)

    fun getSelectedCard(): Card? {
        return cards?.get(selectedCard)
    }

    fun isHidden(token: String):Boolean = pref.getBoolean(token, false)
    fun changeHidden(token: String, visiblity: Boolean) = pref.edit().putBoolean(token, visiblity).apply()

    var cards: List<Card>? = null
    var selectedCard = 0

    var receiverName = ""

    fun Card.toAmount() = if (isHidden(token)) "****" else balance.toAmount()
}
