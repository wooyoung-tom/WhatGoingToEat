package tom.dev.whatgoingtoeat.ui.payment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.payment.PaymentHistoryItem
import tom.dev.whatgoingtoeat.repository.PaymentRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class PaymentHistoryViewModel
@Inject
constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private val _startLoadingDialogEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val startLoadingDialogEvent: LiveData<Any> get() = _startLoadingDialogEvent

    private val _stopLoadingDialogEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val stopLoadingDialogEvent: LiveData<Any> get() = _stopLoadingDialogEvent

    private fun startLoading() {
        _startLoadingDialogEvent.call()
    }

    private fun stopLoading() {
        _stopLoadingDialogEvent.call()
    }

    private val _paymentHistoryLiveData: SingleLiveEvent<List<PaymentHistoryItem>> = SingleLiveEvent()
    val paymentHistoryLiveData: LiveData<List<PaymentHistoryItem>> get() = _paymentHistoryLiveData

    private val _paymentHistoryEmptyLiveData: SingleLiveEvent<Any> = SingleLiveEvent()
    val paymentHistoryEmptyLiveData: LiveData<Any> get() = _paymentHistoryEmptyLiveData

    fun findPaymentHistory(userId: Long) {
        compositeDisposable.add(
            paymentRepository.findPaymentHistory(userId)
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code) {
                        "Success" -> {
                            if (it.histories.isEmpty()) _paymentHistoryEmptyLiveData.call()
                            else _paymentHistoryLiveData.postValue(it.histories)
                        }
                        else -> _paymentHistoryEmptyLiveData.call()
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }
}