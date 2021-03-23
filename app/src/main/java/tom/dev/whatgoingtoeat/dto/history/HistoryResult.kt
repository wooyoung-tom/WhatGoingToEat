package tom.dev.whatgoingtoeat.dto.history

data class HistoryResult(
    val category: String,
    val teamName: String,
    val count: Long,
    val date: String
)
