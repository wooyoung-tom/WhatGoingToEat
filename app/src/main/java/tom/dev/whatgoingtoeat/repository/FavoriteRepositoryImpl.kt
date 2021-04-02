package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse
import tom.dev.whatgoingtoeat.service.FavoriteService
import javax.inject.Inject

class FavoriteRepositoryImpl
@Inject
constructor(
    private val favoriteService: FavoriteService
) : FavoriteRepository {
    override fun findFavoriteRestaurants(userId: Long, category: String, lat: String, lng: String): Single<RestaurantResponse> {
        return favoriteService.findFavoriteRestaurants(userId, category, lat, lng)
    }
}