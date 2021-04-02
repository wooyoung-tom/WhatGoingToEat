package tom.dev.whatgoingtoeat.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantItem
import tom.dev.whatgoingtoeat.repository.RestaurantRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val restaurantRepository: RestaurantRepository
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

    private val _restaurantListLiveData: SingleLiveEvent<List<RestaurantItem>> = SingleLiveEvent()
    val restaurantListLiveData: LiveData<List<RestaurantItem>> get() = _restaurantListLiveData

    fun findRestaurants(category: String, lat: String, lng: String) {
        compositeDisposable.add(
            restaurantRepository.findRestaurants(category, lat, lng)
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _restaurantListLiveData.postValue(it.body)
                }, {
                    it.printStackTrace()
                    Log.e("Restaurant Find Failed", it.message.toString())
                })
        )
    }
}