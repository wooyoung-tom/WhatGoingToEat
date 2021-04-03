package tom.dev.whatgoingtoeat.dto.user

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class User(

    @Json(name = "user_id")
    val id: Long,

    @Json(name = "user_name")
    val name: String
) : Parcelable