package tom.dev.whatgoingtoeat.dto.restaurant

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantEntity(
    val id: Long,
    val restaurantName: String,
    val roadAddress: String?,
    val jibunAddress: String?,
    val phoneNumber: String?,
    val latitude: String,
    val longitude: String,
    val category: String,
    val menuList: List<RestaurantMenu>,
) : Parcelable