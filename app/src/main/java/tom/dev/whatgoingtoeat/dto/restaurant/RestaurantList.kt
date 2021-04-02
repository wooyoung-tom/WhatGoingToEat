package tom.dev.whatgoingtoeat.dto.restaurant

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantList(
    val list: List<RestaurantItem>
): Parcelable
