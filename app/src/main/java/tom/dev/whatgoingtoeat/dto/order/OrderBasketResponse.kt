package tom.dev.whatgoingtoeat.dto.order

data class OrderBasketResponse(
    val restaurantName: String,
    val menuList: List<OrderBasketItem>,
    val totalPrice: Int
)
