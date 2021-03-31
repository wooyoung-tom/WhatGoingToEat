package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantResponse
import tom.dev.whatgoingtoeat.service.RestaurantService
import javax.inject.Inject

class RestaurantRepositoryImpl
@Inject
constructor(
    private val restaurantService: RestaurantService
) : RestaurantRepository {

    override fun findRestaurants(
        category: String,
        lat: String,
        lng: String,
        favorite: Boolean?,
        distance: Boolean?,
        review: Boolean?,
        literal: Boolean?
    ): Single<RestaurantResponse> {
        return restaurantService.findRestaurants(
            category, lat, lng, favorite, distance, review, literal
        )
    }
}