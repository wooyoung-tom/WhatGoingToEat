package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.order.OrderBasketResponse
import tom.dev.whatgoingtoeat.dto.order.OrderDetailEditRequest
import tom.dev.whatgoingtoeat.dto.order.OrderSaveRequest
import tom.dev.whatgoingtoeat.dto.order.OrderSaveResponse
import tom.dev.whatgoingtoeat.service.OrderService
import javax.inject.Inject

class OrderRepositoryImpl
@Inject
constructor(
    private val orderService: OrderService
) : OrderRepository {
    override fun saveOrder(order: OrderSaveRequest): Single<OrderSaveResponse> {
        return orderService.saveOrder(order)
    }

    override fun findOrders(userId: Long): Single<OrderBasketResponse> {
        return orderService.findOrders(userId)
    }

    override fun deleteOrder(orderId: Long): Single<CommonSimpleResponse> {
        return orderService.deleteOrder(orderId)
    }

    override fun editOrder(request: OrderDetailEditRequest): Single<CommonSimpleResponse> {
        return orderService.editOrder(request)
    }
}