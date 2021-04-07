package tom.dev.whatgoingtoeat.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.order.payment.OrderPaymentItem
import tom.dev.whatgoingtoeat.dto.order.payment.PaymentLaterResponse
import tom.dev.whatgoingtoeat.repository.PaymentRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
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

    private val _notPaidPaymentListLiveData: SingleLiveEvent<List<OrderPaymentItem>> = SingleLiveEvent()
    val notPaidPaymentListLiveData: LiveData<List<OrderPaymentItem>> get() = _notPaidPaymentListLiveData

    private val _notPaidPaymentEmptyLiveData: SingleLiveEvent<Any> = SingleLiveEvent()
    val notPaidPaymentEmptyLiveData: LiveData<Any> get() = _notPaidPaymentEmptyLiveData

    fun findNotPaidPayment(id: Long) {
        compositeDisposable.add(
            paymentRepository.findNotPaidPayment(id)
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code) {
                        "Success" -> _notPaidPaymentListLiveData.postValue(it.paymentList)
                        else -> _notPaidPaymentEmptyLiveData.call()
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }
}