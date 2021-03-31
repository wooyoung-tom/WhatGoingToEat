package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse

interface RestaurantService {

    @GET("/market/restaurants")
    fun findRestaurants(
        @Query("category") category: String,
        @Query("lat") latitude: String,
        @Query("lng") longitude: String,
        @Query("favorite") favorite: Boolean? = null,
        @Query("distance") distance: Boolean? = null,
        @Query("review") review: Boolean? = null,
        @Query("literal") literal: Boolean? = null
    ): Single<RestaurantResponse>
}