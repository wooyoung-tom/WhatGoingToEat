package tom.dev.whatgoingtoeat.dto.order.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderPaymentItem(
    val id: Long,
    val method: String,
    val status: String,
    val datetime: String
): Parcelable
