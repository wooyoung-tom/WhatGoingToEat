package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse

interface FavoriteRepository {

    fun findFavoriteRestaurants(userId: Long, category: String, lat: String, lng: String): Single<RestaurantResponse>
}