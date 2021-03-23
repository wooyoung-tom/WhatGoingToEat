package tom.dev.whatgoingtoeat.dto.search

data class SearchMeta(
    val isEnd: Boolean,
    val pageableCount: Int,
    val sameName: SearchSameName,
    val totalCount: Int
)
