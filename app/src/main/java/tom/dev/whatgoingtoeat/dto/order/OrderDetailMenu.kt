package tom.dev.whatgoingtoeat.dto.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu

@Parcelize
data class OrderDetailMenu(
    val id: Long,
    val menu: RestaurantMenu,
    val menuCount: Int
) : Parcelable
