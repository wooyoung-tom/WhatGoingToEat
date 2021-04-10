package tom.dev.whatgoingtoeat.ui.payment.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemPaymentsBinding
import tom.dev.whatgoingtoeat.dto.order.OrderDetailMenu
import tom.dev.whatgoingtoeat.dto.payment.PaymentHistoryItem
import tom.dev.whatgoingtoeat.dto.payment.PaymentHistoryOrderItem

class PaymentHistoryListAdapter : ListAdapter<PaymentHistoryItem, PaymentHistoryListAdapter.PaymentHistoryViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<PaymentHistoryItem>() {
        override fun areItemsTheSame(oldItem: PaymentHistoryItem, newItem: PaymentHistoryItem) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PaymentHistoryItem, newItem: PaymentHistoryItem) = oldItem == newItem
    }

    inner class PaymentHistoryViewHolder(val binding: ItemPaymentsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaymentHistoryItem) {
            binding.tvItemPaymentMethod.text = item.method
            binding.tvItemPaymentDate.text = getDateTimeStr(item.date)
            binding.tvItemPaymentOrder.text = getOrderListStr(item.orders)
            binding.tvItemPaymentOrderPrice.text = getOrderListPriceStr(item.orders)
            binding.tvItemPaymentPrice.text = getPaymentPriceStr(item.price)
        }

        private fun getDateTimeStr(localDateTime: String): String {
            val date = localDateTime.split("T")[0]

            val dateList = date.split("-")
            val year = dateList[0]
            val month = dateList[1]
            val day = dateList[2]
            val fullDateStr = "${year}년 ${month}월 ${day}일"

            return "$fullDateStr ${localDateTime.split("T")[1]}"
        }

        private fun getOrderListStr(list: List<PaymentHistoryOrderItem>): String {
            var orderText = ""
            list.forEachIndexed { index, order ->
                orderText += if (index == list.size - 1) {
                    order.restaurantName
                } else {
                    "${order.restaurantName}\n"
                }
            }

            return orderText
        }

        private fun getOrderListPriceStr(list: List<PaymentHistoryOrderItem>): String {
            var priceText = ""
            list.forEachIndexed { index, order ->
                priceText += if (index == list.size - 1) {
                    "${order.orderPrice} 원"
                } else {
                    "${order.orderPrice} 원\n"
                }
            }

            return priceText
        }

        private fun getPaymentPriceStr(price: Int) = "$price 원"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        return PaymentHistoryViewHolder(
            ItemPaymentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}