package tom.dev.whatgoingtoeat.dto.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderBasketItem(
    val orderId: Long,
    val restaurant: OrderRestaurant,
    val totalPrice: Int,
    val orderDetailList: List<OrderDetailMenu>
) : Parcelable
