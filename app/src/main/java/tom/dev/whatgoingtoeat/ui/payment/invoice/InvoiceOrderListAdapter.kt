package tom.dev.whatgoingtoeat.ui.payment.invoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemInvoiceBinding
import tom.dev.whatgoingtoeat.dto.order.OrderBasketItem
import tom.dev.whatgoingtoeat.dto.order.OrderDetailMenu

class InvoiceOrderListAdapter(
    private val orderList: List<OrderBasketItem>
) : RecyclerView.Adapter<InvoiceOrderListAdapter.PaymentReadyOrderViewHolder>() {

    inner class PaymentReadyOrderViewHolder(val binding: ItemInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderBasketItem) {
            binding.tvItemBasketRestaurantName.text = item.restaurant.name
            binding.tvItemBasketPrice.text = getOrderPriceStr(item.totalPrice)
            binding.tvItemBasketMenu.text = getOrderMenuListStr(item.orderDetailList)
            binding.tvItemBasketMenuPrice.text = getOrderMenuListPriceStr(item.orderDetailList)
        }

        private fun getOrderPriceStr(price: Int) = "$price 원"

        private fun getOrderMenuListStr(list: List<OrderDetailMenu>): String {
            var menuText = ""
            list.forEachIndexed { index, orderDetailMenu ->
                menuText += if (index == list.size - 1) {
                    "${orderDetailMenu.menu.name} (${orderDetailMenu.menuCount})"
                } else {
                    "${orderDetailMenu.menu.name} (${orderDetailMenu.menuCount})\n"
                }
            }

            return menuText
        }

        private fun getOrderMenuListPriceStr(list: List<OrderDetailMenu>): String {
            var priceText = ""
            list.forEachIndexed { index, orderDetailMenu ->
                priceText += if (index == list.size - 1) {
                    "${(orderDetailMenu.menu.price * orderDetailMenu.menuCount)} 원"
                } else {
                    "${(orderDetailMenu.menu.price * orderDetailMenu.menuCount)} 원\n"
                }
            }

            return priceText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentReadyOrderViewHolder {
        return PaymentReadyOrderViewHolder(
            ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PaymentReadyOrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = orderList.size

    private fun getItem(position: Int) = orderList[position]
}