package tom.dev.whatgoingtoeat.dto.restaurant

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RestaurantEntity(
    @Json(name = "id")
    val id: Long,

    @Json(name = "restaurant_name")
    val restaurantName: String,

    @Json(name = "road_address")
    val roadAddress: String?,

    @Json(name = "jibun_address")
    val jibunAddress: String?,

    @Json(name = "phone_number")
    val phoneNumber: String?,

    @Json(name = "latitude")
    val latitude: String,

    @Json(name = "longitude")
    val longitude: String,

    @Json(name = "category")
    val category: String
) : Parcelable