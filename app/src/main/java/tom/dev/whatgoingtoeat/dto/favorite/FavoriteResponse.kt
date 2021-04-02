package tom.dev.whatgoingtoeat.dto.favorite

import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantEntity

data class FavoriteResponse(
    val id: Long,
    val restaurant: RestaurantEntity
)