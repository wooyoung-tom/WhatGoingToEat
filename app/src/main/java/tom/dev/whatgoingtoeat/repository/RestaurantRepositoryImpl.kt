package tom.dev.whatgoingtoeat.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tom.dev.whatgoingtoeat.dto.restaurant.Restaurant
import tom.dev.whatgoingtoeat.service.RestaurantService
import javax.inject.Inject

class RestaurantRepositoryImpl
@Inject
constructor(
    private val restaurantService: RestaurantService
) : RestaurantRepository {

    override fun findRestaurants(category: String, lat: String, lng: String): Flow<PagingData<Restaurant>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                RestaurantPagingSource(restaurantService, category, lat, lng)
            }
        ).flow
    }
}