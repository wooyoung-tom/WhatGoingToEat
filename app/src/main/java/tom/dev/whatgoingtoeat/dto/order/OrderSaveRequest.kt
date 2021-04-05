package tom.dev.whatgoingtoeat.dto.order

data class OrderSaveRequest(
    val menuList: List<OrderSaveRequestMenu>,
    val restaurantId: Long,
    val userId: Long
)