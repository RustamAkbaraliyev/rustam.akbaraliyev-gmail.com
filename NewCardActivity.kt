package uz.mypay.android.ui.new_cards

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_add_cards.*
import kotlinx.android.synthetic.main.fragment_add_cards.inputPan
import kotlinx.android.synthetic.main.fragment_add_cards.progressBar
import kotlinx.android.synthetic.main.fragment_add_cards.toolbar
import uz.mypay.android.R
import uz.mypay.android.adapters.AddCardListAdapter
import uz.mypay.android.di.dagger.DaggerActivity
import uz.mypay.android.util.*
import uz.mypay.android.viewmodels.AddCardViewModel
import javax.inject.Inject

class NewCardActivity : DaggerActivity(R.layout.fragment_add_cards) {
    @Inject
    lateinit var viewModel: AddCardViewModel

    private val adapter = AddCardListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.backLiveData.observe(this, backObserver)
        viewModel.nameErrorLiveData.observe(this, nameErrorObserver)
        viewModel.loadThemesListLiveData.observe(this, loadThemesListObserver)
        viewModel.errorMessageLiveData.observe(this, errorMessageObserver)
        viewModel.cardBackgroundLiveData.observe(this, selectedCardObserver)
        viewModel.cardNameLiveData.observe(this, cardNameObserver)
        viewModel.cardPanLiveData.observe(this, cardPanObserver)
        viewModel.cardExpireLiveData.observe(this, cardExpireDateObserver)
        viewModel.expiredDateErrorLiveData.observe(this, expireDateErrorObserver)
        viewModel.panErrorLiveData.observe(this, panErrorObserver)
        viewModel.loadingLiveData.observe(this, loadingObserver)
        viewModel.successAddCardLiveData.observe(this, successAddCardObserver)

        buttonSave.setOnClickListener { viewModel.addCard() }
        inputCardName.setOnTextChangeListener { viewModel.updateCardName(it) }
        inputPan.setOnTextChangeListener { viewModel.updateCardPan(it) }
        inputCardExpireDate.setOnTextChangeListener { viewModel.updateCardExpire(it) }
        toolbar.setNavigationOnClickListener { viewModel.back() }

        inputCardName.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.addCard()
                hideKeyboard()
                true
            } else {
                false
            }
        }
        adapter.setOnSelectPositionListener { viewModel.selectBackground(it) }
        adapter.submitList(loadImages())
        listDesignCard.apply {
            adapter = this@NewCardActivity.adapter
            layoutManager = LinearLayoutManager(this@NewCardActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private val backObserver = Observer<Unit> { finish() }
    private val nameErrorObserver = Observer<String> { inputCardName.error = it }
    private val expireDateErrorObserver = Observer<String> { inputCardExpireDate.error = it }
    private val panErrorObserver = Observer<String> { inputPan.error = it }
    private val loadThemesListObserver = Observer<List<Int>> { /*adapter.submitList(it))*/ }
    private val errorMessageObserver = Observer<String> { showMessage(it) }
    private val selectedCardObserver = Observer<Int> { cardImage.setBackgroundImage(adapter.currentList[it].image) }
    private val cardNameObserver = Observer<String> { textCardName.text = it }
    private val cardPanObserver = Observer<String> { textPan.text = it }
    private val cardExpireDateObserver = Observer<String> { textExpireDate.text = it }
    private val successAddCardObserver = Observer<Unit> { viewModel.back() }

    private val loadingObserver =
        Observer<Boolean> {
            if (it) {
                progressBar.visible()
                buttonSave.enable(false)
                inputCardName.enable(false)
                inputPan.enable(false)
                inputCardExpireDate.enable(false)
                listDesignCard.enable(false)
            } else {
                progressBar.gone()
                buttonSave.enable(true)
                inputCardName.enable(true)
                inputPan.enable(true)
                inputCardExpireDate.enable(true)
                listDesignCard.enable(true)

            }

        }


}
