package tom.dev.whatgoingtoeat.dto.restaurant

import tom.dev.whatgoingtoeat.paging.PagingMetaData

data class RestaurantResponse(

    val meta: PagingMetaData,
    val body: List<Restaurant>
)