package tom.dev.whatgoingtoeat.dto.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchDocument(
    val id: String,
    val placeName: String,
    val categoryName: String,
    val categoryGroupCode: String,
    val categoryGroupName: String,
    val phone: String,
    val addressName: String,
    val roadAddressName: String,
    val lng: String,
    val lat: String,
    val placeUrl: String,
    val distance: String
): Parcelable