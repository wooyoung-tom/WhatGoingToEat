package tom.dev.whatgoingtoeat.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    @SerializedName("team_name")
    val teamName: String
): Parcelable
