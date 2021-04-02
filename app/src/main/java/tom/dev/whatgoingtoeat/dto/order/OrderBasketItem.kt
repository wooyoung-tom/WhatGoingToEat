package tom.dev.whatgoingtoeat.dto.order

data class OrderBasketItem(
    val id: Long,
    val orderDetailMarketMenu: OrderDetailMenu,
    val menuCount: Int
)
