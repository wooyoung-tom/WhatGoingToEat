package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.favorite.CheckFavoriteResponse
import tom.dev.whatgoingtoeat.dto.favorite.FavoriteRequest
import tom.dev.whatgoingtoeat.dto.favorite.FavoriteResponse
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse

interface FavoriteRepository {

    fun checkFavorite(userId: Long, restaurantId: Long): Single<CheckFavoriteResponse>

    fun saveFavorite(info: FavoriteRequest): Single<FavoriteResponse>

    fun deleteFavorite(info: FavoriteRequest): Single<FavoriteResponse>
}