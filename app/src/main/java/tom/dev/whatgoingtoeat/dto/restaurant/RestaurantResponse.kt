package tom.dev.whatgoingtoeat.dto.restaurant

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantResponse(

    val category: String,
    val body: List<RestaurantItem>
)