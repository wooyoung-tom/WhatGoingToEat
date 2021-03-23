package tom.dev.whatgoingtoeat.dto.history

data class HistoryCounter(
    val selectedMemberCount: Long,
    val category: String,
    val count: Long
)
