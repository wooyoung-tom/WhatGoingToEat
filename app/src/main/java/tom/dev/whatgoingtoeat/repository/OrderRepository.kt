package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.order.OrderBasketResponse
import tom.dev.whatgoingtoeat.dto.order.OrderDetailEditRequest
import tom.dev.whatgoingtoeat.dto.order.OrderSaveRequest
import tom.dev.whatgoingtoeat.dto.order.OrderSaveResponse

interface OrderRepository {
    fun saveOrder(order: OrderSaveRequest): Single<OrderSaveResponse>
    fun findOrders(userId: Long): Single<OrderBasketResponse>
    fun deleteOrder(orderId: Long): Single<CommonSimpleResponse>
    fun editOrder(request: OrderDetailEditRequest): Single<CommonSimpleResponse>
}