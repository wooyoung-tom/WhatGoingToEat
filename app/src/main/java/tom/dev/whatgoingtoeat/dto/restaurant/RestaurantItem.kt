package tom.dev.whatgoingtoeat.dto.restaurant

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantItem(

    @Json(name = "restaurant")
    val restaurant: RestaurantEntity,

    @Json(name = "distance")
    val distance: Int,

    @Json(name = "review")
    val review: Int,

    @Json(name = "favorite")
    val favorite: Int
)
