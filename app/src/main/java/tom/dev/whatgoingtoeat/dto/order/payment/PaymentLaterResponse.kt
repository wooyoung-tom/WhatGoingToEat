package tom.dev.whatgoingtoeat.dto.order.payment

data class PaymentLaterResponse(
    val code: String,
    val message: String,
    val paymentList: List<OrderPaymentItem>
)
