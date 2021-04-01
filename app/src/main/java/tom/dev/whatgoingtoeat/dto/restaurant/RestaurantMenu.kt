package tom.dev.whatgoingtoeat.dto.restaurant

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantMenu(
    val id: Long,
    val name: String,
    val price: Int
) : Parcelable