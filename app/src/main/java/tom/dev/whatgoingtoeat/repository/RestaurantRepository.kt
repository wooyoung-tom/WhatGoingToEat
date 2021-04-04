package tom.dev.whatgoingtoeat.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tom.dev.whatgoingtoeat.dto.restaurant.Restaurant
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse

interface RestaurantRepository {

    fun findRestaurants(category: String, lat: String, lng: String): Flow<PagingData<Restaurant>>
}