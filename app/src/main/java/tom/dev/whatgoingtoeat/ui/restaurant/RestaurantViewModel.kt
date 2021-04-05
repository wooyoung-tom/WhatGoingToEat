package tom.dev.whatgoingtoeat.ui.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import tom.dev.whatgoingtoeat.dto.restaurant.Restaurant
import tom.dev.whatgoingtoeat.repository.RestaurantRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel
@Inject
constructor(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private var currentCategory: String? = null
    private var currentSearchResult: Flow<PagingData<Restaurant>>? = null

    fun searchRestaurant(category: String, lat: Double, lng: Double): Flow<PagingData<Restaurant>> {
        val lastResult = currentSearchResult
        if (category == currentCategory && lastResult != null) return lastResult
        currentCategory = category
        val newResult = restaurantRepository.findRestaurants(category, lat.toString(), lng.toString())
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}