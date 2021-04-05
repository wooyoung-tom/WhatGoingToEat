package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.favorite.CheckFavoriteResponse
import tom.dev.whatgoingtoeat.dto.favorite.FavoriteRequest
import tom.dev.whatgoingtoeat.service.FavoriteService
import javax.inject.Inject

class FavoriteRepositoryImpl
@Inject
constructor(
    private val favoriteService: FavoriteService
) : FavoriteRepository {

    override fun checkFavorite(userId: Long, restaurantId: Long): Single<CheckFavoriteResponse> {
        return favoriteService.checkFavorite(userId, restaurantId)
    }

    override fun saveFavorite(info: FavoriteRequest): Single<CommonSimpleResponse> {
        return favoriteService.saveFavorite(info)
    }

    override fun deleteFavorite(info: FavoriteRequest): Single<CommonSimpleResponse> {
        return favoriteService.deleteFavorite(info)
    }
}