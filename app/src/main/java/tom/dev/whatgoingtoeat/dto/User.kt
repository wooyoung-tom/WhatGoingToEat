package tom.dev.whatgoingtoeat.dto

import com.google.gson.annotations.SerializedName

data class User(
    val name: String,
    @SerializedName("team_name")
    val teamName: String
)
