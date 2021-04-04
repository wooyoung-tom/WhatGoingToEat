package tom.dev.whatgoingtoeat.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import tom.dev.whatgoingtoeat.dto.restaurant.Restaurant
import tom.dev.whatgoingtoeat.service.RestaurantService
import java.io.IOException

class RestaurantPagingSource(
    private val restaurantService: RestaurantService,
    private val category: String,
    private val latitude: String,
    private val longitude: String
) : PagingSource<Int, Restaurant>() {

    companion object {
        private const val FIRST_PAGE = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Restaurant> {
        val position = params.key ?: FIRST_PAGE

        return try {
            val response = restaurantService.searchRestaurantsByCategory(category, latitude, longitude, position)

            LoadResult.Page(
                data = response.body,
                prevKey = if (position == FIRST_PAGE) null else position - 1,
                nextKey = if (response.meta.end) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Restaurant>): Int? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.restaurantId?.toInt()
        }
    }
}