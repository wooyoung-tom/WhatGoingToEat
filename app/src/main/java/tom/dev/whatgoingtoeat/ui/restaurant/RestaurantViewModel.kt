package tom.dev.whatgoingtoeat.ui.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.favorite.FavoriteResponse
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantItem
import tom.dev.whatgoingtoeat.repository.FavoriteRepository
import tom.dev.whatgoingtoeat.repository.RestaurantRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel
@Inject
constructor(
    private val restaurantRepository: RestaurantRepository,
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

    private val _favoriteListLiveData: SingleLiveEvent<List<RestaurantItem>> = SingleLiveEvent()
    val favoriteListLiveData: LiveData<List<RestaurantItem>> get() = _favoriteListLiveData

    fun findFavoriteRestaurants(userId: Long?, category: String, lat: String, lng: String) {
        if (userId != null) {
            compositeDisposable.add(
                favoriteRepository.findFavoriteRestaurants(userId, category, lat, lng)
                    .doOnSubscribe { startLoading() }
                    .doOnSuccess { stopLoading() }
                    .doOnError { stopLoading() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        _favoriteListLiveData.postValue(it.body)
                    }, {
                        it.printStackTrace()
                    })
            )
        }
    }
}