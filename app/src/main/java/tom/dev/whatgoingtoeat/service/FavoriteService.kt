package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.*
import tom.dev.whatgoingtoeat.dto.favorite.CheckFavoriteResponse
import tom.dev.whatgoingtoeat.dto.favorite.FavoriteRequest
import tom.dev.whatgoingtoeat.dto.favorite.FavoriteResponse
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse

interface FavoriteService {

    @GET("/market/favorites/{id}")
    fun findFavoriteRestaurants(
        @Path("id") userId: Long,
        @Query("category") category: String,
        @Query("lat") latitude: String,
        @Query("lng") longitude: String
    ): Single<RestaurantResponse>

    @GET("/market/favorites")
    fun checkFavorite(
        @Query("user_id") userId: Long,
        @Query("restaurant_id") restaurantId: Long
    ): Single<CheckFavoriteResponse>

    @POST("/market/favorites")
    fun saveFavorite(
        @Body info: FavoriteRequest
    ): Single<FavoriteResponse>

    @DELETE("/market/favorites")
    fun deleteFavorite(
        @Query("user_id") userId: Long,
        @Query("restaurant_id") restaurantId: Long
    ): Single<FavoriteResponse>
}