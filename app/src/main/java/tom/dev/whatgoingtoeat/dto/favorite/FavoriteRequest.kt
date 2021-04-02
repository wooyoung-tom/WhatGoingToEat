package tom.dev.whatgoingtoeat.dto.favorite

data class FavoriteRequest(
    val userId: Long,
    val restaurantId: Long
)
