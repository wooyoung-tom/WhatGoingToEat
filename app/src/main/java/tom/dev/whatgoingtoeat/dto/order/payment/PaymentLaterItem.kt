package tom.dev.whatgoingtoeat.dto.order.payment

import tom.dev.whatgoingtoeat.dto.order.OrderBasketItem

data class PaymentLaterItem(
    val id: Long,
    val method: String,
    val status: String,
    val datetime: String,
    val orderItems: List<OrderBasketItem>
)