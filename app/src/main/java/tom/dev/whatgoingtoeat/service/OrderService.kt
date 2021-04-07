package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.order.*
import tom.dev.whatgoingtoeat.dto.order.payment.OrderPaymentRequest
import tom.dev.whatgoingtoeat.dto.order.payment.OrderPaymentResponse

interface OrderService {

    @POST("/orders")
    fun saveOrder(
        @Body order: OrderSaveRequest
    ): Single<OrderSaveResponse>

    @GET("/orders/{id}")
    fun findOrders(
        @Path("id") userId: Long
    ): Single<OrderBasketResponse>

    @GET("/orders/delete/{id}")
    fun deleteOrder(
        @Path("id") orderId: Long
    ): Single<CommonSimpleResponse>

    @POST("/orders/edit")
    fun editOrder(
        @Body request: OrderDetailEditRequest
    ): Single<CommonSimpleResponse>

    @POST("/orders/payment")
    fun orderPayment(
        @Body request: OrderPaymentRequest
    ): Single<OrderPaymentResponse>
}