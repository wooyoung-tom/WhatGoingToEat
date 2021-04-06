package tom.dev.whatgoingtoeat.ui.basket_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.order.OrderDetailEditRequest
import tom.dev.whatgoingtoeat.dto.order.OrderSaveRequestMenu
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu
import tom.dev.whatgoingtoeat.repository.OrderRepository
import tom.dev.whatgoingtoeat.ui.restaurant_info.SelectedRestaurantMenu
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class BasketEditViewModel
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

    val selectedMenuList = ArrayList<SelectedRestaurantMenu>()

    fun selectMenu(menu: RestaurantMenu) {
        val foundMenu = selectedMenuList.find { it.menu.id == menu.id }

        // 찾은 메뉴가 없으면 추가하고 카운터 1로 초기화.
        if (foundMenu == null) selectedMenuList.add(SelectedRestaurantMenu(menu, 1))
        else {
            // 찾은메뉴가 있으면 카운터를 하나 늘린다.
            val idx = selectedMenuList.indexOf(foundMenu)
            selectedMenuList[idx].count++
        }
    }

    fun removeMenu(menu: RestaurantMenu) {
        val foundMenu = selectedMenuList.find { it.menu.id == menu.id }

        // 찾은 메뉴가 존재하면
        if (foundMenu != null) {
            val idx = selectedMenuList.indexOf(foundMenu)
            if (selectedMenuList[idx].count == 1) {
                selectedMenuList.remove(foundMenu)
            } else selectedMenuList[idx].count--
        }
    }

    private val _editOrderCompleteLiveData: SingleLiveEvent<Any> = SingleLiveEvent()
    val editOrderCompleteLiveData: LiveData<Any> get() = _editOrderCompleteLiveData

    private val _editOrderFailedLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    val editOrderFailedLiveData: LiveData<String> get() = _editOrderFailedLiveData

    fun editOrderDetail(orderId: Long) {
        val map = selectedMenuList.map {
            OrderSaveRequestMenu(it.menu.id, it.count)
        }
        compositeDisposable.add(
            orderRepository.editOrder(OrderDetailEditRequest(orderId, map))
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code) {
                        "Failed" -> _editOrderFailedLiveData.postValue(it.message)
                        else -> _editOrderCompleteLiveData.call()
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }
}