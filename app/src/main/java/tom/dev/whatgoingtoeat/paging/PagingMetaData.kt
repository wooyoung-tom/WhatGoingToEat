package tom.dev.whatgoingtoeat.paging

data class PagingMetaData(
    val end: Boolean,
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalResults: Int
)