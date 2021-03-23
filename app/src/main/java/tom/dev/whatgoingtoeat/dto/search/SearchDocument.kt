package tom.dev.whatgoingtoeat.dto.search

data class SearchDocument(
    val id: String,
    val placeName: String,
    val categoryName: String,
    val categoryGroupCode: String,
    val categoryGroupName: String,
    val phone: String,
    val addressName: String,
    val roadAddressName: String,
    val lat: String,
    val lng: String,
    val placeUrl: String,
    val distance: String
)