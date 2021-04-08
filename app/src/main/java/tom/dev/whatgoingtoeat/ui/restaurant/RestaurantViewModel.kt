package tom.dev.whatgoingtoeat.ui.restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import tom.dev.whatgoingtoeat.dto.restaurant.Restaurant
import tom.dev.whatgoingtoeat.repository.RestaurantRepository
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

    fun searchRestaurant(
        category: String, lat: Double, lng: Double, userId: Long = 0,
        favorite: Boolean = false,
        literal: Boolean = false,
        distance: Boolean = false,
        review: Boolean = false
    ): Flow<PagingData<Restaurant>> {
        return when {
            favorite -> restaurantRepository
                .findFavoriteRestaurants(userId, category, lat.toString(), lng.toString(), favorite)
                .cachedIn(viewModelScope)
            literal -> restaurantRepository
                .findRestaurantsByLiteralAsc(category, lat.toString(), lng.toString(), literal = true)
                .cachedIn(viewModelScope)
            distance -> restaurantRepository
                .searchRestaurantByDistanceAsc(category, lat.toString(), lng.toString(), distance = true)
                .cachedIn(viewModelScope)
            review -> restaurantRepository
                .searchRestaurantByReviewDesc(category, lat.toString(), lng.toString(), review = true)
                .cachedIn(viewModelScope)
            else -> restaurantRepository
                .findRestaurants(category, lat.toString(), lng.toString())
                .cachedIn(viewModelScope)
        }
    }
}