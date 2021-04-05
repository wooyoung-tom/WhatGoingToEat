package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tom.dev.whatgoingtoeat.dto.order.OrderBasketResponse
import tom.dev.whatgoingtoeat.dto.order.OrderSaveRequest
import tom.dev.whatgoingtoeat.dto.order.OrderSaveResponse

interface OrderService {

    @POST("/orders")
    fun saveOrder(
        @Body order: OrderSaveRequest
    ): Single<OrderSaveResponse>

    @GET("/orders/{id}")
    fun findOrders(
        @Path("id") userId: Long
    ): Single<OrderBasketResponse>
}