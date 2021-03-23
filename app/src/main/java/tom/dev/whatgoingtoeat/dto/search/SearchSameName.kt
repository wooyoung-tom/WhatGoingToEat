package tom.dev.whatgoingtoeat.dto.search

data class SearchSameName(
    val keyword: String,
    val region: List<String>,
    val selectedRegion: String
)
