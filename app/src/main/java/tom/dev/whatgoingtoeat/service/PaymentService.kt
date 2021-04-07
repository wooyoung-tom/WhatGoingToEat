package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import tom.dev.whatgoingtoeat.dto.order.payment.PaymentLaterResponse

interface PaymentService {

    @GET("/payment/{id}")
    fun findNotPaidPayment(
        @Path("id") id: Long
    ): Single<PaymentLaterResponse>
}