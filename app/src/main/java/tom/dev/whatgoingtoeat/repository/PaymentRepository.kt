package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.order.payment.PaymentLaterResponse
import tom.dev.whatgoingtoeat.dto.payment.PaymentHistoryResponse

interface PaymentRepository {

    fun findNotPaidPayment(id: Long): Single<PaymentLaterResponse>

    fun findPaymentHistory(userId: Long): Single<PaymentHistoryResponse>
}