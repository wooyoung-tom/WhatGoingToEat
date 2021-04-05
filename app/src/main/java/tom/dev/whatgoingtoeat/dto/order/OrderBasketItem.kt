package tom.dev.whatgoingtoeat.dto.order

data class OrderBasketItem(
    val orderId: Long,
    val restaurantName: String,
    val totalPrice: Int,
    val orderDetailList: List<OrderDetailMenu>
)
