package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.*
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.favorite.CheckFavoriteResponse
import tom.dev.whatgoingtoeat.dto.favorite.FavoriteRequest

interface FavoriteService {

    @GET("/favorites/check/{id}")
    fun checkFavorite(
        @Path("id") userId: Long,
        @Query("restaurantId") restaurantId: Long
    ): Single<CheckFavoriteResponse>

    @POST("/favorites")
    fun saveFavorite(
        @Body info: FavoriteRequest
    ): Single<CommonSimpleResponse>

    @POST("/favorites/delete")
    fun deleteFavorite(
        @Body info: FavoriteRequest
    ): Single<CommonSimpleResponse>
}