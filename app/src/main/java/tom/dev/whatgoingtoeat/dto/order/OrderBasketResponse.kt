package tom.dev.whatgoingtoeat.dto.order

data class OrderBasketResponse(
    val code: String,
    val message: String,
    val orders: List<OrderBasketItem>
)
