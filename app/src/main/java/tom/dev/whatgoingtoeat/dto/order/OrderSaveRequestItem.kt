package tom.dev.whatgoingtoeat.dto.order

data class OrderSaveRequestItem(
    val menuId: Long,
    val menuCount: Int
)
