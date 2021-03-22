package tom.dev.whatgoingtoeat.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRequest(
    val name: String,
    @SerializedName("team_name")
    val teamName: String
): Parcelable
