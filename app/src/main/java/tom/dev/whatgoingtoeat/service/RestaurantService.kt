package tom.dev.whatgoingtoeat.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse

interface RestaurantService {

    @GET("/restaurants")
    suspend fun searchRestaurantsByCategory(
        @Query("category") category: String,
        @Query("lat") latitude: String,
        @Query("lng") longitude: String,
        @Query("page") page: Int
    ): RestaurantResponse

    @GET("/restaurants/favorites/{id}")
    suspend fun findFavoriteRestaurants(
        @Path("id") userId: Long,
        @Query("category") category: String,
        @Query("lat") latitude: String,
        @Query("lng") longitude: String
    ): RestaurantResponse
}