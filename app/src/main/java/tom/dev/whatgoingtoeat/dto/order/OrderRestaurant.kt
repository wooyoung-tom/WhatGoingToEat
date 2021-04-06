package tom.dev.whatgoingtoeat.dto.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu

@Parcelize
data class OrderRestaurant(
    val category: String,
    val jibunAddress: String?,
    val latitude: String,
    val longitude: String,
    val phoneNumber: String?,
    val id: Long,
    val name: String,
    val roadAddress: String?,
    val menuList: List<RestaurantMenu>
) : Parcelable
