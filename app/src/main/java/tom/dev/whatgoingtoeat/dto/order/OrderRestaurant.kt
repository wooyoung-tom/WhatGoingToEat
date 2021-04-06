package tom.dev.whatgoingtoeat.dto.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
) : Parcelable
