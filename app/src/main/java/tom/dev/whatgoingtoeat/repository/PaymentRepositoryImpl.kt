package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.order.payment.PaymentLaterResponse
import tom.dev.whatgoingtoeat.dto.payment.PaymentHistoryResponse
import tom.dev.whatgoingtoeat.service.PaymentService
import javax.inject.Inject

class PaymentRepositoryImpl
@Inject
constructor(
    private val paymentService: PaymentService
) : PaymentRepository {
    override fun findNotPaidPayment(id: Long): Single<PaymentLaterResponse> {
        return paymentService.findNotPaidPayment(id)
    }

    override fun findPaymentHistory(userId: Long): Single<PaymentHistoryResponse> {
        return paymentService.findPaymentHistory(userId)
    }
}