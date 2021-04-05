package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.order.OrderBasketResponse
import tom.dev.whatgoingtoeat.dto.order.OrderSaveRequest

interface OrderRepository {
    fun saveOrder(order: OrderSaveRequest): Single<CommonSimpleResponse>
    fun findOrders(userId: Long): Single<OrderBasketResponse>
}