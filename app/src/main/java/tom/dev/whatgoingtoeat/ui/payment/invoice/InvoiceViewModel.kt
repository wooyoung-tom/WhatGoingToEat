package tom.dev.whatgoingtoeat.ui.payment.invoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.order.payment.OrderPaymentItem
import tom.dev.whatgoingtoeat.dto.order.payment.OrderPaymentRequest
import tom.dev.whatgoingtoeat.repository.OrderRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel
@Inject
constructor(
    private val orderRepository: OrderRepository
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

    private val _orderPaidSuccessLiveData: SingleLiveEvent<OrderPaymentItem> = SingleLiveEvent()
    val orderPaidSuccessLiveData: LiveData<OrderPaymentItem> get() = _orderPaidSuccessLiveData

    private val _orderPaidFailedLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    val orderPaidFailedLiveData: LiveData<String> get() = _orderPaidFailedLiveData

    fun orderPayment(orders: List<Long>, method: String) {
        compositeDisposable.add(
            orderRepository.orderPayment(OrderPaymentRequest(orders, method))
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code) {
                        "Success" -> _orderPaidSuccessLiveData.postValue(it.payment)
                        else -> _orderPaidFailedLiveData.postValue(it.message)
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }
}