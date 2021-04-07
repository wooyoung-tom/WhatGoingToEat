package tom.dev.whatgoingtoeat.dto.order.payment

data class OrderPaymentRequest(
    val orders: List<Long>,
    val method: String
)
