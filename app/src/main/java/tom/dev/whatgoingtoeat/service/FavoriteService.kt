package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse

interface FavoriteService {

    @GET("/market/favorites/{id}")
    fun findFavoriteRestaurants(
        @Path("id") userId: Long,
        @Query("category") category: String,
        @Query("lat") latitude: String,
        @Query("lng") longitude: String
    ): Single<RestaurantResponse>
}