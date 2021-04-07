package tom.dev.whatgoingtoeat.dto.order.payment

data class OrderPaymentResponse(
    val code: String,
    val message: String,
    val payment: OrderPaymentItem?
)