package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.order.payment.PaymentLaterResponse

interface PaymentRepository {

    fun findNotPaidPayment(id: Long): Single<PaymentLaterResponse>
}