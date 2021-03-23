package tom.dev.whatgoingtoeat.dto.search

data class SearchResponse(
    val meta: SearchMeta,
    val documents: List<SearchDocument>
)
