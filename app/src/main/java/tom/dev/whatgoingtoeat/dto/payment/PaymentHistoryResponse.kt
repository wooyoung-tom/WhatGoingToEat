package tom.dev.whatgoingtoeat.dto.payment

data class PaymentHistoryResponse(
    val code: String,
    val message: String,
    val histories: List<PaymentHistoryItem>
)