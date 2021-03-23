package tom.dev.whatgoingtoeat.dto.history

data class HistoryResponse<out T>(
    val meta: HistoryMeta,
    val message: String,
    val body: T?
)