package uz.mypay.android.viewmodels.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.mypay.android.R
import uz.mypay.android.data.source.local.CardDataSource
import uz.mypay.android.data.source.remote.api.CardApi
import uz.mypay.android.model.Card
import uz.mypay.android.util.getString
import uz.mypay.android.util.isConnected
import uz.mypay.android.viewmodels.AddCardViewModel
import javax.inject.Inject

class AddCardViewModelImpl @Inject constructor(var cardDataSource: CardDataSource, var api: CardApi) : ViewModel(), AddCardViewModel {

    private val _backLiveData = MutableLiveData<Unit>()
    override val backLiveData: LiveData<Unit> = _backLiveData

    private val _panErrorLiveData = MutableLiveData<String>()
    override val panErrorLiveData: LiveData<String> = _panErrorLiveData

    private val _expiredDateErrorLiveData = MutableLiveData<String>()
    override val expiredDateErrorLiveData: LiveData<String> = _expiredDateErrorLiveData

    private val _nameErrorLiveData = MutableLiveData<String>()
    override val nameErrorLiveData: LiveData<String> = _nameErrorLiveData

    private val _loadingLiveData = MutableLiveData<Boolean>()
    override val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private val _errorMessageLiveData = MutableLiveData<String>()
    override val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    private val _cardBackgroundLiveData = MutableLiveData<Int>()
    override val cardBackgroundLiveData: LiveData<Int> = _cardBackgroundLiveData

    private val _loadThemesListLiveData = MutableLiveData<List<Int>>()
    override val loadThemesListLiveData: LiveData<List<Int>> = _loadThemesListLiveData

    private val _cardNameLiveData = MutableLiveData<String>()
    override val cardNameLiveData: LiveData<String> = _cardNameLiveData

    private val _cardPanLiveData = MutableLiveData<String>()
    override val cardPanLiveData: LiveData<String> = _cardPanLiveData

    private val _cardExpireLiveData = MutableLiveData<String>()
    override val cardExpireLiveData: LiveData<String> = _cardExpireLiveData

    private val _successAddCardLiveData = MutableLiveData<Unit>()
    override val successAddCardLiveData: LiveData<Unit> = _successAddCardLiveData

    override fun addCard() {
        when {
            _cardPanLiveData.value?.length ?: 0 < 19 -> _panErrorLiveData.value =
                getString(R.string.required_fill)
            _cardExpireLiveData.value?.length ?: 0 < 5 -> _expiredDateErrorLiveData.value =
                getString(R.string.required_fill)
            _cardNameLiveData.value.isNullOrEmpty() -> _nameErrorLiveData.value =
                getString(R.string.required_fill)
            !isConnected() -> _errorMessageLiveData.value =
                getString(R.string.alert_message_no_network)
            else -> {
                _loadingLiveData.value = true
                viewModelScope.launch {
                    val expire = _cardExpireLiveData.value.toString()
                    val mm = expire.take(2)
                    val yy = expire.takeLast(2)
                    val pan = _cardPanLiveData.value.toString().replace(" ", "")
                    val card = Card(
                        name = _cardNameLiveData.value.toString(),
                        expiresAt = "$yy$mm",
                        number = pan,
                        imageId = (_cardBackgroundLiveData.value ?: 0).toString(),
                        isMain = cardDataSource.cards?.isEmpty() ?: true
                        /*gradient = Gradient("F8869B", "fAAE96", 1, false)*/
                    )
                    Log.d("XXX"," is Main ${cardDataSource.cards?.isEmpty() ?: true}")
                    Log.d("XXX"," is Main posted ${card.isMain}")

                    try {
                        val response = api.add(card)
                        _loadingLiveData.postValue(false)
                        if (response.isSuccessful) {
                            _successAddCardLiveData.postValue(Unit)
                        } else {
                            _errorMessageLiveData.postValue(response.message())
                        }
                    } catch (e: Exception) {
                        _errorMessageLiveData.postValue(e.message)
                    } finally {
                        _loadingLiveData.postValue(false)
                    }
                }
            }
        }
    }

    override fun updateCardName(name: String) {
        _cardNameLiveData.value = name
    }

    override fun updateCardPan(pan: String) {
        _cardPanLiveData.value = pan
    }

    override fun updateCardExpire(date: String) {
        _cardExpireLiveData.value = date
    }

    override fun selectBackground(index: Int) {
        if (_loadingLiveData.value != true) {
            _cardBackgroundLiveData.value = index
        }
    }

    override fun back() {
        _backLiveData.value = Unit
    }
}