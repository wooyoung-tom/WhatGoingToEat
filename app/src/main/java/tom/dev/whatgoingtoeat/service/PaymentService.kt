package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import tom.dev.whatgoingtoeat.dto.order.payment.PaymentLaterResponse
import tom.dev.whatgoingtoeat.dto.payment.PaymentHistoryResponse

interface PaymentService {

    @GET("/payment/{id}")
    fun findNotPaidPayment(
        @Path("id") id: Long
    ): Single<PaymentLaterResponse>

    @GET("/payment/history/{id}")
    fun findPaymentHistory(
        @Path("id") userId: Long
    ): Single<PaymentHistoryResponse>
}