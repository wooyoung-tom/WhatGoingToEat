package tom.dev.whatgoingtoeat.dto.history

import com.google.gson.annotations.SerializedName

data class History(
    val name: String,
    @SerializedName("team_name")
    val teamName: String,
    @SerializedName("date")
    val historyDate: String,
    val category: String
)