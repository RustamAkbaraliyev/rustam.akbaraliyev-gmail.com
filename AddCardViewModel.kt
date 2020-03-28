package uz.mypay.android.viewmodels

import androidx.lifecycle.LiveData

interface AddCardViewModel {
    val backLiveData: LiveData<Unit>
    val panErrorLiveData: LiveData<String>
    val expiredDateErrorLiveData: LiveData<String>
    val nameErrorLiveData: LiveData<String>
    val loadingLiveData: LiveData<Boolean>
    val errorMessageLiveData: LiveData<String>
    val cardBackgroundLiveData: LiveData<Int>
    val loadThemesListLiveData: LiveData<List<Int>>
    val cardNameLiveData: LiveData<String>
    val cardPanLiveData: LiveData<String>
    val cardExpireLiveData: LiveData<String>
    val successAddCardLiveData:LiveData<Unit>

    fun addCard()
    fun selectBackground(index: Int)
    fun back()
    fun updateCardName(name: String)
    fun updateCardPan(pan: String)
    fun updateCardExpire(date: String)
}