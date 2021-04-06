package tom.dev.whatgoingtoeat.dto.order

data class OrderDetailEditRequest(
    val orderId: Long,
    val menuList: List<OrderSaveRequestMenu>
)
