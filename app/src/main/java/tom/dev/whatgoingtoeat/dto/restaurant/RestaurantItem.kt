package tom.dev.whatgoingtoeat.dto.restaurant

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class RestaurantItem(

    @Json(name = "restaurant")
    val restaurant: RestaurantEntity,

    @Json(name = "distance")
    val distance: Int,

    @Json(name = "review")
    val review: Int,

    @Json(name = "favorite")
    val favorite: Int
): Parcelable
