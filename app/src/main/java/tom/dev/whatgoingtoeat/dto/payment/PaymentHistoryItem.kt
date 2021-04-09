package tom.dev.whatgoingtoeat.dto.payment

import java.time.LocalDateTime

data class PaymentHistoryItem(
    val id: Long,
    val method: String,
    val date: String,
    val orders: List<PaymentHistoryOrderItem>,
    val price: Int
)
