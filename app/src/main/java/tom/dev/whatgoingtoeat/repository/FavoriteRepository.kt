package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.favorite.CheckFavoriteResponse
import tom.dev.whatgoingtoeat.dto.favorite.FavoriteRequest

interface FavoriteRepository {

    fun checkFavorite(userId: Long, restaurantId: Long): Single<CheckFavoriteResponse>

    fun saveFavorite(info: FavoriteRequest): Single<CommonSimpleResponse>

    fun deleteFavorite(info: FavoriteRequest): Single<CommonSimpleResponse>
}