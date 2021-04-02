package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.order.OrderBasketResponse
import tom.dev.whatgoingtoeat.dto.order.OrderSaveRequestItem
import tom.dev.whatgoingtoeat.dto.order.OrderSaveResponse

interface OrderRepository {
    fun saveOrder(userId: Long, order: List<OrderSaveRequestItem>): Single<OrderSaveResponse>
    fun findOrders(userId: Long): Single<List<OrderBasketResponse>>
}