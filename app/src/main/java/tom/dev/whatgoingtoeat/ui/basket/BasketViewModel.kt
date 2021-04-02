package tom.dev.whatgoingtoeat.ui.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.order.OrderBasketResponse
import tom.dev.whatgoingtoeat.repository.OrderRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class BasketViewModel
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

    private val _orderListLiveData: SingleLiveEvent<List<OrderBasketResponse>> = SingleLiveEvent()
    val orderListLiveData: LiveData<List<OrderBasketResponse>> get() = _orderListLiveData

    fun findReadyStateOrders(userId: Long?) {
        if (userId != null) {
            compositeDisposable.add(
                orderRepository.findOrders(userId)
                    .doOnSubscribe { startLoading() }
                    .doOnSuccess { stopLoading() }
                    .doOnError { stopLoading() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        _orderListLiveData.postValue(it)
                    }, {
                        it.printStackTrace()
                    })
            )
        }
    }
}