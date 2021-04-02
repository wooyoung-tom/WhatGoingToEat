package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.order.OrderBasketResponse
import tom.dev.whatgoingtoeat.dto.order.OrderSaveRequestItem
import tom.dev.whatgoingtoeat.dto.order.OrderSaveResponse
import tom.dev.whatgoingtoeat.service.OrderService
import javax.inject.Inject

class OrderRepositoryImpl
@Inject
constructor(
    private val orderService: OrderService
) : OrderRepository {
    override fun saveOrder(userId: Long, order: List<OrderSaveRequestItem>): Single<OrderSaveResponse> {
        return orderService.saveOrder(userId, order)
    }

    override fun findOrders(userId: Long): Single<List<OrderBasketResponse>> {
        return orderService.findOrders(userId)
    }
}