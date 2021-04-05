package tom.dev.whatgoingtoeat.dto.favorite

data class CheckFavoriteResponse(
    val code: String,
    val message: String,
    val favorite: Boolean
)