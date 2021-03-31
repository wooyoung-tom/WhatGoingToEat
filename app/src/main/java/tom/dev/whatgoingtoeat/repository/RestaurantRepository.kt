package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import retrofit2.http.Query
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse

interface RestaurantRepository {

    fun findRestaurants(
        category: String,
        lat: String,
        lng: String,
        favorite: Boolean? = null,
        distance: Boolean? = null,
        review: Boolean? = null,
        literal: Boolean? = null
    ): Single<RestaurantResponse>
}