package tom.dev.whatgoingtoeat.dto.restaurant

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    val category: String,
    val jibunAddress: String?,
    val latitude: String,
    val longitude: String,
    val phoneNumber: String?,
    val restaurantId: Long,
    val restaurantName: String,
    val roadAddress: String?,


    val menuList: List<RestaurantMenu>,
    val distance: Int,
    val reviewCount: Int,
    val favoriteCount: Int
) : Parcelable