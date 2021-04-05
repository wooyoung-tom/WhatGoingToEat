package tom.dev.whatgoingtoeat.ui.restaurant_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.favorite.FavoriteRequest
import tom.dev.whatgoingtoeat.dto.order.OrderSaveRequest
import tom.dev.whatgoingtoeat.dto.order.OrderSaveRequestMenu
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu
import tom.dev.whatgoingtoeat.repository.FavoriteRepository
import tom.dev.whatgoingtoeat.repository.OrderRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class RestaurantInfoViewModel
@Inject
constructor(
    private val orderRepository: OrderRepository,
    private val favoriteRepository: FavoriteRepository
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

    private val _orderSaveCompleteEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val orderSaveCompleteEvent: LiveData<Any> get() = _orderSaveCompleteEvent

    fun saveOrder(userId: Long?, restaurantId: Long) {
        if (userId == null) return

        val requestOrder = OrderSaveRequest(
            menuList = selectedMenuList.map { selectedRestaurantMenu ->
                OrderSaveRequestMenu(selectedRestaurantMenu.menu.id, selectedRestaurantMenu.count)
            },
            restaurantId = restaurantId,
            userId = userId
        )


        compositeDisposable.add(
            orderRepository.saveOrder(requestOrder)
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _orderSaveCompleteEvent.call()
                }, {
                    it.printStackTrace()
                })
        )
    }

    private val _isMyFavoriteLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val isMyFavoriteLiveData: LiveData<Boolean> get() = _isMyFavoriteLiveData

    fun checkFavorite(userId: Long?, restaurantId: Long) {
        if (userId == null) return

        compositeDisposable.add(
            favoriteRepository.checkFavorite(userId, restaurantId)
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _isMyFavoriteLiveData.postValue(it.checkFavorite)
                }, {
                    it.printStackTrace()
                })
        )
    }

    private val _deleteSuccess: SingleLiveEvent<Any> = SingleLiveEvent()
    val deleteSuccess: LiveData<Any> get() = _deleteSuccess

    fun deleteFavorite(userId: Long?, restaurantId: Long) {
        if (userId == null) return

        compositeDisposable.add(
            favoriteRepository.deleteFavorite(userId, restaurantId)
                .doOnSubscribe { startLoading() }
                .doOnError { stopLoading() }
                .doOnSuccess { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code == "SUCCESS") {
                        _deleteSuccess.call()
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }

    private val _saveSuccess: SingleLiveEvent<Any> = SingleLiveEvent()
    val saveSuccess: LiveData<Any> get() = _saveSuccess

    fun saveFavorite(userId: Long?, restaurantId: Long) {
        if (userId == null) return

        val info = FavoriteRequest(userId, restaurantId)

        compositeDisposable.add(
            favoriteRepository.saveFavorite(info)
                .doOnSubscribe { startLoading() }
                .doOnError { stopLoading() }
                .doOnSuccess { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code == "SUCCESS") {
                        _saveSuccess.call()
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }
}