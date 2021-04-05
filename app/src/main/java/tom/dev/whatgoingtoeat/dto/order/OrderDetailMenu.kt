package tom.dev.whatgoingtoeat.dto.order

import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu

data class OrderDetailMenu(
    val id: Long,
    val menu: RestaurantMenu,
    val menuCount: Int
)
