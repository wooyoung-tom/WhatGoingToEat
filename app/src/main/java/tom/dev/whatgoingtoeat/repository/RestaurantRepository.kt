package tom.dev.whatgoingtoeat.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tom.dev.whatgoingtoeat.dto.restaurant.Restaurant

interface RestaurantRepository {

    fun findRestaurants(category: String, lat: String, lng: String): Flow<PagingData<Restaurant>>

    fun findRestaurantsByLiteralAsc(category: String, lat: String, lng: String, literal: Boolean): Flow<PagingData<Restaurant>>

    fun findFavoriteRestaurants(
        userId: Long, category: String, lat: String, lng: String, favorite: Boolean
    ): Flow<PagingData<Restaurant>>
}