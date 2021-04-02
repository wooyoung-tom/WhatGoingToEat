package tom.dev.whatgoingtoeat.dto.favorite

data class CheckFavoriteResponse(
    val favoriteId: Long,
    val checkFavorite: Boolean
)