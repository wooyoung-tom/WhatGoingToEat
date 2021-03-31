package tom.dev.whatgoingtoeat.ui.restaurant_info

import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu

data class SelectedRestaurantMenu(
    val menu: RestaurantMenu,
    var count: Int = 0
)